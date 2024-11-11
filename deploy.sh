#!/bin/bash

# 设置 Docker 镜像仓库地址
DOCKER_REPO="swr.cn-north-4.myhuaweicloud.com/oomall-yyds/productdemoaop"
TAG="0.0.1"  # 固定版本标签

# 1. 更新 Git 代码
echo "正在拉取最新代码..."
git pull || { echo "Git 拉取失败！"; exit 1; }

# 2. 构建 Maven 项目并跳过测试
echo "正在构建 Maven 项目，跳过测试..."
mvn pre-integration-test -Dmaven.test.skip=true || { echo "Maven 构建失败！"; exit 1; }

# 3. 获取构建的 Docker 镜像 ID（假设 Docker 镜像标签为 `0.0.1-SNAPSHOT`）
IMAGE_ID=$(docker images -q xmu-javaee/productdemoaop:0.0.1-SNAPSHOT)

if [ -z "$IMAGE_ID" ]; then
  echo "未找到 Docker 镜像 'xmu-javaee/productdemoaop'！请检查构建过程。"
  exit 1
fi

# 4. Docker 登录
echo "正在登录 Docker 镜像仓库..."
docker login -u cn-north-4@WQRFCOIQJCDBKCWLEV1P -p e6e630046ba3cbca9dcba8e0536e7fc7d595036063e63e1f3b6f2aa329968222 swr.cn-north-4.myhuaweicloud.com || { echo "Docker 登录失败！"; exit 1; }

# 5. 给 Docker 镜像打上新标签并覆盖掉
echo "正在为 Docker 镜像打标签，版本为 $TAG..."
docker tag $IMAGE_ID $DOCKER_REPO:$TAG || { echo "Docker 打标签失败！"; exit 1; }

# 6. 推送到远程仓库
echo "正在推送 Docker 镜像到远程仓库..."
docker push $DOCKER_REPO:$TAG || { echo "Docker 推送失败！"; exit 1; }

# 7. 更新 Docker Swarm 服务
echo "正在更新 Docker Swarm 服务..."
docker service update --image $DOCKER_REPO:$TAG productdemoaop || { echo "更新 Docker 服务失败！"; exit 1; }

# 8. 完成
echo "部署完成！新的镜像已成功推送并更新到服务：$DOCKER_REPO:$TAG"
