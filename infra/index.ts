import * as kx from "@pulumi/kubernetesx";
import * as pulumi from "@pulumi/pulumi";
import * as k8s from '@pulumi/kubernetes';
import * as aws from "@pulumi/aws";

let config = new pulumi.Config();
let stack = pulumi.getStack();
const appName = 'sample-service';
const imageTag = config.get('IMAGE_TAG') ?? 'latest';
const numReplicas = (config.get('NUM_REPLICAS') ?? 1) as number;
const repo = config.require('ECR_REPO');
const image = `${repo}:${imageTag}`;
const containerPort = 8080;
const appLabels = {app: appName};
const hostname = `${appName}.${stack}.delfidx.io`;

//FIXME: use the provider from the stack reference (once it is exported), instead of the default config from local kubeconfig
// const env = pulumi.getStack();
// const infra = new pulumi.StackReference(`lis-infra/${env}`);
// const provider = new k8s.Provider("k8s", { kubeconfig: infra.getOutput("kubeConfig") });
// then specify the opts to Deployment and Service using: {provider: provider}

const deployment = new kx.Deployment(`${appName}-dep`, {
    spec: {
        selector: {matchLabels: appLabels},
        replicas: numReplicas,
        template: {
            metadata: {labels: appLabels},
            spec: {
                containers: [{
                    name: appName,
                    image: image,
                    ports: [
                        {
                            name: 'http',
                            containerPort: containerPort
                        }
                    ],
                    readinessProbe: {
                        httpGet: {
                            path: '/health/readiness',
                            port: containerPort
                        },
                        initialDelaySeconds: 5,
                        timeoutSeconds: 3
                    },
                    livenessProbe: {
                        httpGet: {
                            path: '/health/liveness',
                            port: containerPort
                        },
                        initialDelaySeconds: 5,
                        timeoutSeconds: 3,
                        failureThreshold: 10
                    }
                }]
            }
        }
    }
});

const service = new kx.Service(appName, {
    metadata: {name: appName},
    spec: {
        selector: appLabels,
        type: "NodePort",
        ports: [
            {
                protocol: "TCP",
                port: containerPort
            }
        ]
    }
});

const ingress = new k8s.networking.v1.Ingress('Ingress', {
    metadata: {
        name: `${appName}-ingress`,
        annotations: {
            'kubernetes.io/ingress.class': 'alb',
            'alb.ingress.kubernetes.io/scheme': 'internal',
            'alb.ingress.kubernetes.io/group.name': 'alb-services-group',
            'alb.ingress.kubernetes.io/listen-ports':  '[{"HTTP": 80}, {"HTTPS":443}]',
            'alb.ingress.kubernetes.io/certificate-arn': 'arn:aws:acm:us-west-2:016272216798:certificate/3a4e90be-5640-4c15-9dba-af35fa5dbbeb',
            'alb.ingress.kubernetes.io/ssl-policy': 'ELBSecurityPolicy-TLS-1-2-2017-01',
        }
    },
    spec: {
        rules: [
            {
                host: hostname,
                http: {
                    paths: [
                        {
                            pathType: "Prefix",
                            path: "/",
                            backend: {
                                service: {
                                    name: appName,
                                    port: {number: containerPort}
                                }
                            }
                        }
                    ]
                }
            }
        ]
    }
});
const route53Zone = aws.route53.getZone({
    name: "delfidx.io",
    privateZone: true
});
export const ingressHostName = ingress.status.loadBalancer.ingress[0].hostname;

const cname = new aws.route53.Record(`${appName}-cname`, {
    zoneId: route53Zone.then(r => r.zoneId),
    name: hostname,
    type: "CNAME",
    ttl: 300,
    allowOverwrite: true,
    records: [ingressHostName],
});
export const deploymentName = deployment.metadata.name;
export const serviceId = service.id;
export const serviceDNS = cname.name;