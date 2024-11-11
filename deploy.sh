#!/bin/bash

# Docker 镜像名
DOCKER_IMAGE="swr.cn-north-4.myhuaweicloud.com/oomall-yyds/productdemoaop:0.0.1"
LOCAL_IMAGE="xmu-javaee/productdemoaop:0.0.1-SNAPSHOT"

# 删除 Docker 镜像
echo "正在删除 '$LOCAL_IMAGE' 和 '$DOCKER_IMAGE' 镜像..."
docker rmi $LOCAL_IMAGE $DOCKER_IMAGE || { echo "删除 Docker 镜像失败，可能镜像不存在或已经被删除。"; }

# 1. 更新 Git 代码
echo "正在拉取最新代码..."
git pull || { echo "Git 拉取失败！"; exit 1; }

# 2. 编译代码、构建镜像
echo "正在使用 Maven 编译代码、构建镜像..."
mvn pre-integration-test -Dmaven.test.skip=true || { echo "Maven 构建失败！"; exit 1; }

# 3. Docker 镜像服务登录
echo "正在登录 Docker 镜像仓库..."
docker login -u cn-north-4@WQRFCOIQJCDBKCWLEV1P -p e6e630046ba3cbca9dcba8e0536e7fc7d595036063e63e1f3b6f2aa329968222 swr.cn-north-4.myhuaweicloud.com || { echo "Docker 登录失败！"; exit 1; }

# 4. 给 Docker 镜像打标签
echo "正在为 Docker 镜像打标签，版本为 0.0.1 ..."
docker tag $LOCAL_IMAGE $DOCKER_IMAGE || { echo "Docker 打标签失败！"; exit 1; }

# 5. 推送到镜像仓库
echo "正在推送 Docker 镜像到华为云镜像仓库..."
docker push $DOCKER_IMAGE || { echo "Docker 推送失败！"; exit 1; }

# 6. SSH 登录到另一台机器，拉取最新的 Docker 镜像
echo "正在通过 SSH 登录到 OOMALL-node1 并拉取最新镜像..."
sshpass -p 'wxhyyds_0727' ssh -T root@192.168.1.146 << EOF
  docker pull $DOCKER_IMAGE
EOF

# 7. 更新 Docker Swarm 服务
echo "正在更新 Docker Swarm 服务..."
docker service update --image $DOCKER_IMAGE productdemoaop || { echo "更新 Docker 服务失败！"; exit 1; }

# 8. 完成
echo "部署完成！新的镜像已成功推送并更新到服务：$DOCKER_IMAGE"

# 9. 检查服务状态
echo "正在检查服务状态..."
docker service ps productdemoaop
