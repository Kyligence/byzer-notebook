pipeline {
    agent {
        kubernetes {
yaml """
spec:
  containers:
  - name: "package"
    command:
    - "cat"
    image: "457798666374.dkr.ecr.us-west-2.amazonaws.com/jenkinsslave/kaniko_zen:alpine"
    imagePullPolicy: "Always"
    resources:
      limits:
        memory: "4096Mi"
        cpu: "3000m"
      requests:
        memory: "4096Mi"
        cpu: "3000m"
    tty: true
    volumeMounts:
    - mountPath: "/jenkins-common"
      name: "volume-0"
      readOnly: false
    - name: "docker-config"
      mountPath: /kaniko/.docker/
    - name: "maven-settings"
      mountPath: "/usr/share/java/maven-3/conf/settings.xml"
      readOnly: false
      subPath: "settings.xml"
  - name: "jnlp"
    image: "jenkins/inbound-agent:4.3-4"
    resources:
      requests:
        cpu: "100m"
        memory: "256Mi"
    volumeMounts:
    - name: "volume-0"
      mountPath: "/jenkins-common"
      readOnly: false
  volumes:
  - name: "volume-0"
    persistentVolumeClaim:
      claimName: "jenkins-common"
      readOnly: false
  - name: "docker-config"
    configMap:
      name: "docker-config-auth"
  - name: "maven-settings"
    configMap:
      name: "maven-settings"
      defaultMode: 0777
      items:
      - key: "settings.xml"
        path: "settings.xml"
"""
        }
    }

    stages {
        stage('Build and Push Byzer_Notebook Image') {
            steps {
                container('package') {
                    timestamps {
                        sh "/tools/dotnet-gitversion . /output buildserver"
                        script {
                            def props = readProperties file: 'gitversion.properties'

                            env.GitVersion_MajorMinorPatch = props.GitVersion_MajorMinorPatch
                            env.GitVersion_PreReleaseLabel = props.GitVersion_PreReleaseLabel
                            env.GitVersion_ShortSha = props.GitVersion_ShortSha
                        }
                        sh "sed -i 's#10.1.30.102:8081#devops-nexus:8081#g' pom.xml"
                        sh "sed -i 's#https://devopsnexus.kyligence.io#http://devops-nexus:8081#g' pom.xml"
                        dir('build') {
                            sh "./package.sh "
                            sh "/kaniko/executor --dockerfile=../Dockerfile --destination=registry.kyligence.io/kyligence/yzer-notebook:${GitVersion_MajorMinorPatch}-${GitVersion_PreReleaseLabel}-${GitVersion_ShortSha} --context=dir://\$(pwd)"
                        }
                    }
                }
            }
        }
    }
}

