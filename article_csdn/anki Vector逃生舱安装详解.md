# anki Vector逃生舱安装详解



vector的escape pod（逃生舱）的githut地址：https://github.com/cyb3rdog/escapepod-docker





#### 准备的机器环境

* win10
* 安装docker（如果安装自行百度）



#### 开始

* 先用git下载脚本; 

  `git clone https://github.com/cyb3rdog/escapepod-docker.git`

* 右键使用管理员打开：Git Bash

  ![image-20210602160651729](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602160651729.png)

  

  

* 切换到下载的脚本目录：（注意：路径“/”）

  `cd D:/git_projects/escapepod-docker/windows`

 * 然后按README.md操作，执行：

  `./DOCKER-ESCAPEPOD-START.bat`

  

#### 问题解决

* (如果直接cmd打开，会报：'docker-machine' 不是内部或外部命令，也不是可运行的程序；)
   (如果Git Bash非用管理员执行`./DOCKER-ESCAPEPOD-START.bat`，会报：没权限；)



* 如果报：VBoxManage not found. Make sure VirtualBox is installed and VBoxManage is in the path
  	//解决：需要安装VirtualBox-6.1.22-144080-Win.exe

* 如果报：Error with pre-create check: "This computer is running Hyper-V. VirtualBox won't boot a 64bits VM when Hyper-V is activated. Either use Hyper-V as a driver, or disable the Hyper-V hypervisor. (To skip this check, use --virtualbox-no-vtx-check)"

​	      //解决：`在DOCKER-ESCAPEPOD-START.bat的第52行增加：--virtualbox-no-vtx-check，如下图`

![image-20210602171403289](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602171403289.png)



* 执行：`docker-machine active`报错：

  Error getting IP address: ssh command error:
	command : ip addr show
	err     : exit status 255
	//解决：直接docker-machine ip ESCAPEPOD，能查到ip就行



#### 检查程序是否正常

查看VirtualBox是否启动：

![image-20210602172324236](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602172324236.png)

查看容器是否启动：

![image-20210602172514744](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602172514744.png)

#### 本机配置

在C:\Windows\System32\drivers\etc的hosts增加一行：

```shell
127.0.0.1 escapepod.local
```

保存文件.如图

![image-20210602171910156](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602171910156.png)

直接访问即可： http://escapepod.local/

![image-20210602172606185](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602172606185.png)





#### 程序的说明

#### 程序位置

查询容器：`docker ps`

![image-20210602172918866](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602172918866.png)

进入容器：`docker exec -it 3ee08a650800  /bin/bash`

![image-20210602174905602](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602174905602.png)

escape pod的路径：`/usr/local/escapepod`

mongodb位置：`/usr/local/bin`

dbPath: `/var/lib/mongodb`

#### 配置文件

配置文件：escape-pod.conf

root@escapepod:/usr/local# cat /etc/escape-pod.conf

```shell
DDL_RPC_PORT=8084
DDL_HTTP_PORT=8085
DDL_OTA_PORT=8086
DDL_UI_PORT=80

DDL_SAYWHATNOW_STT_MODEL=/usr/local/escapepod/model.tflite
DDL_SAYWHATNOW_STT_SCORER=/usr/local/escapepod/model.scorer

DDL_DB_NAME=database
DDL_DB_HOST=127.0.0.1
DDL_DB_PASSWORD=MzBmMWFmY2NhYzE0
DDL_DB_PORT=27017
DDL_DB_USERNAME=myUserAdmin
```


root@escapepod:/usr/local# cat /etc/mongod.conf

```she
# mongod.conf

# for documentation of all options, see:
#   http://docs.mongodb.org/manual/reference/configuration-options/

# Where and how to store data.
storage:
  dbPath: /var/lib/mongodb
  journal:
    enabled: true
#  engine:
#  mmapv1:
#  wiredTiger:

# where to write logging data.
systemLog:
  destination: file
  logAppend: true
  path: /var/log/mongodb/mongod.log

# network interfaces
net:
  port: 27017
  bindIp: 127.0.0.1


# how the process runs
processManagement:
  timeZoneInfo: /usr/share/zoneinfo

#security:

#operationProfiling:

#replication:

#sharding:

## Enterprise-Only Options:

#auditLog:

#snmp:
replication:
  replSetName: rs0


security:
  authorization: enabled
  keyFile: /etc/mongod.key
```



#### 复制程序到本地主机：

```she
#退回主宿机窗口

exit
#复制escapepod到主宿机d盘
docker cp 3ee08a650800:/usr/local/escapepod d:/
#复制mongodb到主宿机d盘mongodb
docker cp 3ee08a650800:/usr/local/bin d:/mongodb
```





#### 命令的安装

ubuntu
安装端口查询命令：
`apt-get install lsof`

![image-20210602193223661](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602193223661.png)



安装网络工具 ifconfig
`apt-get install net-tools`



root@escapepod:/home/ubuntu# qemu-arm-static -version
qemu-arm version 4.2.1 (Debian 1:4.2-3ubuntu6.14)
Copyright (c) 2003-2019 Fabrice Bellard and the QEMU Project developers



#### 数据库连接

* 修改主宿机与容器同一网络，增加：--network=host

`docker run -it --rm --name escapepod --network=host -d cyb3rdog/escapepod:latest`

* 修改 mongod.conf文本支持远程访问：

`bindIp: 127.0.0.1改为bindIp: 0.0.0.0`

* 使用“Navicat Premium 15”连接即可；如图

![image-20210602194043231](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602194043231.png)

数据库的所有表只有2张：intents、licenses

![image-20210602195014294](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602195014294.png)

intents表内容：

![image-20210602195021403](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602195021403.png)



licenses表内容：

![image-20210602195032160](anki%20Vector%E9%80%83%E7%94%9F%E8%88%B1%E5%AE%89%E8%A3%85%E8%AF%A6%E8%A7%A3.assets/image-20210602195032160.png)



#### 总结

* **安装完才发现escape pod（逃生舱）也需要license**；
* **escapepod 目前不懂反编释；**

