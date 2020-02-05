# 华北电力大学志愿活动系统
## 软件概要设计
本软件使用前后端分离的设计模式。不使用jsp，使用html网页中的js脚本进行前后端通信。
    
    在前后端分离模式下，如何解决模型与页面的绑定问题？
    后台除了主页以外，不再负责控制页面的跳转与返回。页面的跳转皆有前端js完成。

本软件使用maven管理依赖，tomcat作为web服务器。

软件后台架构为 spring boot + spring data jpa + spring mvc。

后台采用经典的分层结构，依次分为视图控制器controller，控制器操作的对象vo，
持久实体类entity，数据库访问层repository，业务逻辑层service。

## 软件详细设计
### 实体类设计

#### 志愿活动

志愿活动生命周期：
<ul>
<li>CONFIRMING 部门已发送等待审核</li>
<li>SEND 审核并修改</li>
<li>APPLY 报名</li>
<li>STARTED 录用并开始活动</li>
<li>FINISHED 结束并评价</li>
</ul>
共5个阶段。

志愿活动的报名十分复杂，结合具体案例分析，例如，在2019级迎新活动中，有多个
岗位，同一个岗位也可以分为不同地点，与时段。最终学生按照单一时间段报名。

因此，设计Activity类中包含多个ActivityStation，即一场志愿活动中有多个岗位；
设计ActivityStation中有多个ActivityPeriod，即一个岗位中分多个时间段，最终按
时间段报名。

#### 学生与志愿活动的关系

多个学生参加多个志愿活动，这是多对多关系，而且这个多对多关系是多层的，蕴含了
例如志愿报名结果，志愿成绩等信息。学生参加志愿活动分为如下流程：

<ul>
     <li>APPLIED 已报名</li>
     <li>PASSED 已审核</li>
     <li>EVALUATED 已评价</li>
</ul>

因此新建一个关联表Record即志愿记录类来解决这个问题。

#### 多级评论

允许添加评论的评论，因此评论是一个树状结构，只用如下方法处理树状结构的储存问题。

```
    /**
     * 父级评论，没有父级则为空
     */
    @ManyToOne(targetEntity = Comment.class)
    @JoinColumn(name = "parent_comment", referencedColumnName = "id")
    private Comment parentComment;

    @OneToMany(targetEntity = Comment.class, mappedBy = "parentComment")
    private List<Comment> sonComment;
```

#### 图片实体类的多用性 

Image实体类用于储存图片，可以是Student或Department的头像，也可以是评论与
活动介绍所带内容。该类与多个实体类保持多对一关系，减小了多个图片表造成的冗余。

#### 数据表结构

![img](./readmeSrc/dbo.png)

### 业务逻辑设计
#### 登录 

设计UserVo类，用于接收用户填写信息。在业务逻辑中自动判断用户身份，跳转向不同主页。

#### 账户信息修改

由于密码修改的特殊逻辑，这里不修改密码，只修改其他信息。

#### 密码修改

在点击*找回密码邮件*中的超链接之后，或者在*修改密码页面*输入原来密码之后，
就可以直接修改密码。

找回密码邮件中的超链接应包含加密的用户信息，在跳转之后进行解码判断是什么用户， 
从而修改密码。
#### 添加志愿活动

志愿活动的一次性添加

志愿活动按照ActivityPeriod进行报名，但增删改均以Activity为大类。而志愿活动的
查询既可以通过关联查询获得。

志愿活动的修改与删除

### web作用域限定
#### session作用域

UserId 学生或部门的id
UserCategory "Student" "Department" "Admin"

#### applicationContext作用域

path 服务器地址

## 前后端通信设计

### 通用接口

#### 登录，获取用户部分信息    [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/login

发送 post application/json 

    id  //用户名
    password //密码
    示例：{"id":"12345","password":"12345"}
返回 UserInfo转成的json
    示例1：
    {
        "name"："教务处"//部门名
        "manager":"李华"//负责人姓名
        "e-mail":"12345@123.com"//负责人邮箱
        "phone_num":123456798749//部门负责人联系电话
    }
    示例2：
    {
        “id”：120181080701//学号
        “school”:"控计学院"//学院
        “major”:"软件工程"//专业
        "grade"："2018级"//年级
        “class”:"软件1802"//班级
        “name”："邵博深"//学生姓名
        “sex”:“男”//性别
        “phone_num”：15311780285//手机号
    }

前端应根据其中的userCategory属性跳转至
    {
        "userCategory":1,合适的页面：-1-登录失败 0-管理员 1-学生 2-部门
```
        "student":{"studentNum":"120171020201",
        ... 学生其他信息
        },"department":null
    }
    {
    "userCategory": 2,
    "student": null,
    "department": {
        "id": "6177001",
        "name": "图书馆（主）",
        "phoneNum": null,
        "email": null,
        "manager": null,
        "images": [
            {
                "name": null,
                "url": "https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E5%A4%B4%E5%83%8F&step_word=&hs=0&pn=36&spn=0&di=224840&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=4163580791%2C1286387910&os=1384471263%2C3542048267&simid=0%2C0&adpicid=0&lpn=0&ln=3718&fr=&fmq=1579421350054_R&fm=result&ic=&s=undefined&hd=&latest=&copyright=&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=head&bdtype=0&oriquery=&objurl=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201610%2F15%2F20161015073047_nTMaz.thumb.700_0.jpeg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B17tpwg2_z%26e3Bv54AzdH3Fks52AzdH3F%3Ft1%3D08888ambb&gsm=&rpstart=0&rpnum=0&islist=&querylist=&force=undefined"
            }
        ]
    }
    }
```

#### 登出
http://192.168.43.1:8888/volunteer/logout

发送
    
    无
返回

    主页 index.html

#### 图片上传
http://192.168.43.1:8888/volunteer/uploadImage

发送 post multipart/form-data

    file 文件本身

返回 json

    {uploadImage:success,url:图片的url}
    {uploadImage:error}

#### 修改密码
http://192.168.43.1:8888/volunteer/changePassword

发送 post application/x-www-form-urlencoded

    oldPassword 老密码
    password 新密码

返回 json

    {changePassword:success}
    {changePassword:error}

#### 发送验证邮件
http://192.168.43.1:8888/volunteer/sendEmail

发送 post application/x-www-form-urlencoded

    id 用户id

返回 json

    {sendEmail:success}
    {sendEmail:error}

#### 发表评论
http://192.168.43.1:8888/volunteer/comment/save

发送 post application/json

```
    private String id;//添加id时为修改，不添加为新增
    private String content;//评论内容
    private ArrayList<ImageVo> imageVos;//数组
    private String parentCommentId;//父评论id，若无则不写
    private String activityId;//必填
```
示例:
```
{
        "parentCommentId": null,
        "content": "老师很棒",
        "activityId": "001",
        "images": []
}
```
返回 json 包含保存评论的id
```
{
    "result": {
        "commentId": "2fc14154-4f8a-4ef3-902e-39b40ee29666",
        "save": "no parent comment found but result success"
    }
}
```
#### 删除评论
http://192.168.43.1:8888/volunteer/comment/delete

发送 post application/x-www-form-urlencoded
```
commentId 要删除的评论id
```

返回 无 

#### 获取特定活动的评论区（分页） [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/comment/getComments

发送 post application/x-www-form-urlencoded
```
activity 该活动的id
size 每页活动数
page 页码
```

返回 json数组
示例：activity = 001 size = 3 page = 0
```
[
    {
        "id": "3001",
        "time": "2019-03-13 20:30:13.0",
        "parentCommentId": null,
        "content": "老师很棒",
        "distributorCategory": "student",
        "distributorName": "邵博深",
        "distributorId": "120181080701",
        "activityId": "001",
        
    },
    {
        "id": "3002",
        "time": "2019-03-13 17:51:00.0",
        "parentCommentId": null,
        "content": "认真",
        "distributorCategory": "department",
        "distributorName": "图书馆（主）",
        "distributorId": "6177001",
        "activityId": "001",
    },
    {
        "id": "3004",
        "time": "2020-01-27 07:56:37.0",
        "parentCommentId": "3006",
        "content": "asdf",
        "distributorCategory": "student",
        "distributorName": "葛翰臣",
        "distributorId": "120171020201",
        "activityId": "001",
    
    }
]
```
#### 查询活动信息
http://192.168.43.1:8888/volunteer/query/activity

发送 post application/x-www-form-encoded

activityId或stationId或periodId

返回 查询的最外层activity的所有信息



### 部门接口
#### 修改部门信息
http://192.168.43.1:8888/volunteer/department/updateDepartment

发送post application/json

    phoneNum 
    manager
    email
    profiles //与上个接口相同

#### 获取已发送的活动信息  [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/department/query/released

发送 post 无内容

返回 json
``` 

[
    {
        "id": "001",//该条活动的id
        "name": "2019图书馆公益活动",//活动名
        "description": "搬运书籍，贴标签等",//活动描述
        "departmentName": "图书馆（主）",//部门名
        "status": 1,//当前所处的时期——审核期
        "stations": [
            {
                "periods": [
                    {
                        
                        "startDate": "2020-01-11 02:23:37.0",//开始时间
                        "endDate": "2020-01-20 02:22:58.0",//结束时间
                        "equDuration": 20,//等效时长
                        "amountRequired": 20//所需人数
                    },
                ]
            },
        ]
    }
    {
        "id": "002",//该条活动的id
        "name": "图书馆搬书",//活动名
        "description": "搬运书籍，贴标签等",//活动描述
        "departmentName": "图书馆（主c）",//部门名
        "status": 1,//当前所处的时期——审核期
        "stations": [
            {
                "periods": [
                    {
                        
                        "startDate": "2020-02-11 02:23:37.0",//开始时间
                        "endDate": "2020-01-20 02:22:58.0",//结束时间
                        "equDuration": 20,//等效时长
                        "amountRequired": 20//所需人数
                    },
                ]
            },
        ]
    }
                
]
```

#### 获取特定活动时间段的报名记录  [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/department/query/records

发送 post application/x-www-form-urlencoded

```
periodId 时间段
```

返回 json
```
[
    {
        "id": "02",
        "volunteerId": "120181080702",//学生学号
        "periodId": "1",
        "info": "玩耍……",//学生的自我介绍
        
        "auditLevel": 0,
        "evaluation": "被录取而一直未到",
        "stars": 0,
        "comment": null,
        "passed": true
    },
    {
        "id": "05",
        "volunteerId": "120181080704",
        "periodId": "1",
        "info": "试试吧",
        "status": 0,
        "auditLevel": null,
        "evaluation": null,
        "stars": 0,
        "comment": null,
        "passed": false
    },
    {
        "id": "06",
        "volunteerId": "120181080703",
        "periodId": "1",
        "info": "放心",
        "status": 0,
        "auditLevel": null,
        "evaluation": null,
        "stars": 0,
        "comment": null,
        "passed": false
    }
]
```

#### 获取特定学生信息  [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/department/query/studentInfo

发送 post application/x-www-form-urlencoded

```
studentId 学生学号
```

返回 json
```
{
    "studentNum": "120171020201",//学号
    "name": "葛翰臣",//姓名
    "major": "电气工程及其自动化",//专业
    "grade": "2017级",//年级
    "classs": "电气1710"，//班级
    "phoneNum": 136546546,//学生手机号
    "school": "电气学院",//学院
    “sex”："男"//性别
    “record”：[
        "info":"我觉得我能胜任"//学生报名时的个人介绍
    ]
}
```


#### 新建活动  [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/department/service/saveActivity

发送 post application/json

```
id //添加id时为修改，不添加为新增
name//活动名称 
description//活动内容介绍
departmentId//部门id

```
示例：
```
{
    "name": "2019图书馆公益活动4",//活动名称 
    "description": "搬运书籍，贴标签等",//活动内容介绍
    "departmentId": "6177001",//部门id
}
```

返回  json 其中包含已保存活动的id信息

```
{"save activity": "0779c44d-4034-47d0-93bf-2d49829ed7ed"}
```

#### 为已有活动添加活动地点  [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/department/service/saveStation

发送 post application/json

```
id//添加id时为修改，不添加为新增
name//劳动地点
linkman//联系人
phoneNum//联系人的电话或手机号
parentId //活动的id
```

```
{
    "name": "主C 101",//劳动地点
    "linkman": "张三",//联系人
    "phoneNum": "61772591",//联系人的电话或手机号
    "parentId": "6177001"//活动的id
}
```
返回  json 其中包含报存活动地点的id信息

#### 为已有活动地点添加时间段  [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/department/service/savePeriod

发送 post application/json

```

startDate//开始日期，格式： 2020-01-11 02:23:37.0
endDate//结束日期
timePeriod//每日工作时间，以字符串表示每天几点开始几点结束
requirements//录取要求
equDuration//等效时长
amountRequired//所需人数
```

示例：
```
{
    "startDate": "2020-01-11 02:23:37.0",//开始日期
    "endDate": "2020-01-20 02:22:58.0",//结束日期
    "timePeriod": "上午8点开始，下午不用来",//每日工作时间，
    "requirements": “男同学优先录取”,//录取要求
    "equDuration": 20,//等效时长
    "amountRequired": 20//所需人数
}
```
返回  json 其中包含报存活动时间段的id信息

#### 删除活动，活动地点或活动时间段
http://192.168.43.1:8888/volunteer/department/service/cancel

发送 post application/x-www-form-urlencoded

下面三个属性三选一
```
activityId //要删除活动的id
stationId //要删除活动地点的id
periodId //要删除活动时间段的id

```

不返回内容

#### 批量审核报名
http://192.168.43.1:8888/volunteer/department/service/approve

发送 json数组
通过审核的报名记录（record）的id

不返回内容

#### 批量评价打分  [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/department/service/evaluate

发送 json数组
```
recordId//报名记录（record）的id
evaluate//评语
auditLevel//4个等级，不通过：0 ，通过：1，良好：2，优秀：3
示例：
[
    {
        recordId:001//报名记录（record）的id
        evaluate:null//评语
        auditLevel：3//等级
    }
    {
        recordId:001//报名记录（record）的id
        evaluate:该同学很热情//评语
        auditLevel：3//等级
    }
    {
        recordId//报名记录（record）的id
        evaluate：“一直没来”//评语，等级为“0”，必须填理由
        auditLevel：0//等级
    }

]
```




不返回内容






## 学生接口
#### 修改学生信息
http://192.168.43.1:8888/volunteer/student/updateStudent

发送post application/json

```
    name 姓名
    phoneNum 电话号码
    email 邮箱
    profiles json数组，图片对象，包含name,url两个属性
```

示例：
```
    {
        "email":"12345",
        "name":"12",
        "phoneNum":"1234",
        "profiles":[
                {"name":"1234","url":"123456"},
                {"name":"1234","url":"123456"}
            ]
    }
```

注意，对于新的图片，要通过图片上传接口上传并得到图片的url，再加入profiles的
json数组中；若未上传新的图片，则保持原信息不变即可。

返回 json

    成功{"updateResult":"success"}
    失败{"updateResult":"failed"}


#### 获取可报名的活动信息  [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/student/query/findIndexActivities

发送 post 无信息

返回 json数组

示例：
```
[
    {
        “state”：0//当前状态，可供选择：0，已选择、取消选择：1
        "name": "2018图书馆公益活动",//活动名称
        "description": "打杂",//活动内容介绍
        "stations": [
            {
                "name": "图书馆主馆305",//劳动地点
                "periods": [
                    {
                       
                        "startDate": "2020-01-27 21:34:13",//开始时间
                        "endDate": "2020-02-27 21:34:13",//结束时间
                        "timePeriod": "每天中午12点-下午1点",//每日工作时间
                        "requirements": null,//录取要求
                        "equDuration": 15,//劳动时长
                        "amountRequired": 14,//所需人数
                        "amountSigned": 11//已报人数
                    }
                ]
            }
        ]
    }
    
    {
        “state”：1//当前状态，可供选择：0，已选择、取消选择：1
        "name": "财务处算账",//活动名称
        "description": "打杂",//活动内容介绍
        "stations": [
            {
                "name": "D305",//劳动地点
                "periods": [
                    {
                       
                        "startDate": "2020-01-27 21:34:13",//开始时间
                        "endDate": "2020-02-27 21:34:13",//结束时间
                        "timePeriod": "每周二下午2点到5点",//每日工作时间
                        "requirements": 诚实守信,//录取要求
                        "equDuration": 30,//劳动时长
                        "amountRequired": 5//所需人数
                        "amountSigned": 11//已报人数
                    }
                ]
            }
        ]
    }

    ……
]
```

#### 获取自己的公益劳动报名记录  [2020.2.6 修改]
http://192.168.43.1:8888/volunteer/student/query/getRecord

发送 post 
status 可取 0,1,2,3 也可以不带此参数


返回 json数组

示例：
```
[
    {
        “state”：0//当前状态，待审核：0，进行中：1，待部门评价：2
        "name": "2018图书馆公益活动",//活动名称
        "description": "打杂",//活动内容介绍
        "stations": [

            {
                "name": "图书馆主馆305",//劳动地点
                "linkman": "王五",//联系人
                "phoneNum": "61773241",//联系电话
                "periods": [
                    {
                       
                        "startDate": "2020-01-27 21:34:13",//开始时间
                        "endDate": "2020-02-27 21:34:13",//结束时间
                        "timePeriod": "每天中午12点-下午1点",//每日工作时间
                        "requirements": null,//录取要求
                        "equDuration": 15,//劳动时长
                        "amountRequired": 14,//所需人数
                        
                    }
                ]
            }
        ]
    }

    {
         “state”：2//当前状态，待审核：0，进行中：1，待部门评价：2
        "name": "财务处",//活动名称
        "description": "打杂",//活动内容介绍
        "stations": [

            {
                "name": "D305",//劳动地点
                "linkman": "湛山",//联系人
                "phoneNum": "61775241",//联系电话
                "periods": [
                    {
                       
                        "startDate": "2020-01-27 21:34:13",//开始时间
                        "endDate": "2020-02-27 21:34:13",//结束时间
                        "timePeriod": "每天中午12点-下午1点",//每日工作时间
                        "requirements": null,//录取要求
                        "equDuration": 15,//劳动时长
                        
                    }
                ]
            }
        ]
    }


    ……
]
```

#### 报名参加活动
http://192.168.43.1:8888/volunteer/student/service/participate

发送 post application/json 

```
    periodId //活动时间段的id
    info //报名备注信息
```

返回 json 成功或失败

#### 取消报名
http://192.168.43.1:8888/volunteer/student/service/cancel

发送 post application/a-www-form-urlencoded

```
    recordId 报名记录的id
```

返回 json 成功或失败

#### 活动结束后评价
http://192.168.43.1:8888/volunteer/student/service/appraise

发送 post application/json

```
    recordId
    comment//学生评价
    stars//评星1-5
```

返回 json 成功或失败

### 管理员接口

#### 
```
管理员接口文档 [2020.2.6 添加]

账号管理模块

创建账号
发送  post application/json
     
 Id_Type//所创建账号的类型；学生：1   部门：2
      Id//账号

示例1
{
“Id_Type”:1//类型为学生账号
“Id”:120181080701
“name”：“邵博深”
“class”：软件802
“grade”：2018级
“major”：软件工程
“sex”：男

}
示例2
{
“Id_Type”:2//类型为部门账号
“Id”:6177009
“name”：财务处
“manager”：刘惠东//部门负责人
}

返回 json数组
    若创建的是部门账号：
成功{"updatedepartment":"success"}
失败{"updatedepartment":"failed"}
若创建的是学生账号：
成功{"updatestudent":"success"}
失败{"updatestudent":"failed"}

密码重置
发送  post application/x-www-form-urlencoded
      
Id//部门或者学生的账号

返回 json数组
{changePassword:success}
{changePassword:error}

账号删除
发送  post application/x-www-form-urlencoded
 Id//部门或者学生的账号
返回json  
若创建的是部门账号：
成功{"updatedepartment":"success"}
失败{"updatedepartment":"failed"}
若创建的是学生账号：
成功{"updatestudent":"success"}
失败{"updatestudent":"failed"}


活动审批模块
获取部门已提交活动
发送  post 无内容

返回json数组
    示例1：
    [
        {   "id": "001 "//该条活动的id
            "name": "2019图书馆公益活动"，//活动名
            "description": "搬运书籍，贴标签等"，//活动描述
            "departmentName": "图书馆（主）",//部门名
            "status": 1,//当前所处的时期——审核期
            "stations": [
            {
                   "periods": [
                    {
                        "startDate": "2020-01-11 02:23:37",//开始时间
                        "endDate": "2020-01-20 02:22:58",//结束时间
                        "equDuration": 20,//等效时长
                        "amountRequired": 20//人数要求
                    }
                    ]
            }
            ]
        }
]    


审批部门活动
发送 post application/x-www-form-urlencoded
        On_off//是否开启审批权限，开启：1  关闭：0
Activity_id//所审批活动的id
Result//开启审批权限下的审批结果，通过：1  未通过：0
            示例1：
                {
    “On_off”:0//已关闭审核权限
}
            示例2：
                {
    “On_off”:1//已开启审核权限
    “Activity_id”:001//所选审批活动的id
    “Result”：1//通过
}
            示例3：
                {
    “On_off”:1//已开启审核权限
    “Activity_id”:001//所选审批活动的id
    “Result”：0//未通过
}
    
返回 json数组
{updateactivity:success}
{updateactivity:error}

管理员已审批（含关闭审核权限时）的审批记录
        发生 post 无内容
        返回json数组
    示例1：
    [
        {   "id": "001 "//该条活动的id
            "name": "2019图书馆公益活动"，//活动名
            "description": "搬运书籍，贴标签等"，//活动描述
            "departmentName": "图书馆（主）",//部门名
            "status": 1,//status如果仍是1，则是审核未通过 
            "stations": [
            {
                   "periods": [
                    {
                        "startDate": "2020-01-11 02:23:37",//开始时间
                        "endDate": "2020-01-20 02:22:58",//结束时间
                        "equDuration": 20,//等效时长
                        "amountRequired": 20//人数要求
                    }
                    ]
            }
            ]
        }
]    
    示例2：
    [
        {   "id": "001 "//该条活动的id
            "name": "2019图书馆公益活动"，//活动名
            "description": "搬运书籍，贴标签等"，//活动描述
            "departmentName": "图书馆（主）",//部门名
            "status": 2,//已通过审核，status不是0或1，则为已通过
            "stations": [
            {
                  "periods": [
                   {
                       "startDate": "2020-01-11 02:23:37",//开始时间
                       "endDate": "2020-01-20 02:22:58",//结束时间
                       "equDuration": 20,//等效时长
                       "amountRequired": 20//人数要求
                   }
                   ]
            }
            ]
        }
]    
示例2：
[
   {    "id": "001 "//该条活动的id
        "name": "2019图书馆公益活动"，//活动名
        "description": "搬运书籍，贴标签等"，//活动描述
        "departmentName": "图书馆（主）",//部门名
            "status": 3,//已通过审核，status不是0或1，则为已通过
            "stations": [
            {
                   "periods": [
                    {
                        "startDate": "2020-01-11 02:23:37",//开始时间
                        "endDate": "2020-01-20 02:22:58",//结束时间
                        "equDuration": 20,//等效时长
                        "amountRequired": 20//人数要求
                    }
                    ]
                    }
                    ]
        }
]


活动查询模块
活动查询
    发送  post application/x-www-form-urlencoded
        下面两个属性任输其一：
            Id//部门或者学生的账号
            Name//部门或者学生的名字
                5示例1：
                {“id”：6177001}//部门的id
示例2：
                {“id”：120181080701}//学生的id（学号）
示例3：
                {“name”：图书馆（主）}//部门的名字
示例4：
                {“name”：邵博深}//学生的名字
    返回 json
                示例1：
                [
                {   
                    “id”：6177001//部门的id
“name”：图书馆（主）//部门名
                    “manager”：丁松//部门负责人名
                    “stars”：4//学生给部门的总评星
                    “activity”:[
                            {
                            “name”:图书馆扫地//活动名
                            “description”：打扫卫生，每天下午2点到//活动内容
                            “Station”[
                                “period”[
                                {
                                    “start_data”：2020-4-10 08:00:00//开始时间
                                    “end_data”：2020-5-7 17:50:00//结束时间
                                    “equ_duration”：20//等效时长
                                    “amount_required”:10//所需人数
}   
]
“Record”[
    “comment”:{
“老师很好”，//a学生的评论
“不咋地”，//b学生的评论
“太累了”//c学生的评论
“……”//其他学生里评论
                                        }
]
]                       
                        
                        “name”:图书馆搬书//活动名
                            “description”：整理书籍,每天上午9点到//活动内容
                            “Station”[
                                “period”[
                                {
                                    “start_data”：2020-4-11 08:00:00//开始时间
                                    “end_data”：2020-5-7 12:50:00//结束时间
                                    “equ_duration”：40//等效时长
                                    “amount_required”:56//所需人数
}   
]
“Record”[
    “comment”:{
“老师很好”，//a学生的评论
“不咋地”，//b学生的评论
“太累了”//c学生的评论
“……”//其他学生里评论

                        }
                    ]
}
]

            示例2：
            [
            {   
                “name”:邵博深//学生名
                “id”:120181080701//id（学号）
                “school”:控计学院//学院
                “major”：软件工程//专业
                “class”：软件1802//班级
                “total_duration”:40
                “activity”[
                {
“name”：图书馆搬书//活动名
“description”：每天下午来搬书//活动描述
                    “record”[
        “evaluation”:该学生很认真//部门就该活动给的评价
                            ]



“name”：图书馆扫地//活动名
“description”：每天上午来扫地//活动描述
                    “record”[
        “evaluation”:该学生没来//部门就该活动给的评价
                            ]


“name”：财务处算账//活动名
“description”：核对账目//活动描述
                    “record”[
        “evaluation”：//部门就该活动给的评价（此处空白，表示未写评价）
                            ]

                }   
                ]
            }
            ]


批量导出模块
批量导出学生记录
    发送 post application/x-www-form-urlencoded
        Grade//年级
示例1：
                “grade”：2018级
            示例2：
                “grade”：2019级
返回 excle文件下载
    示例1：
        [
            {
“name”：邵博深//学生名
“id”：120181080701//学号
“school”:控计学院//学院
                    “major”：软件工程//专业
                    “grade”:2018级//年级
                    “class”：软件1802//班级
                    “total_duration”:40//总时长

“name”：谢沅伯//学生名
“id”：120181080702//学号
“school”:控计学院//学院
                    “major”：信息安全//专业
                    “grade”:2018级//年级
                    “class”：信安1801//班级
                    “total_duration”:60//总时长


……//同上

}
        ]

```






##软件调试

### jpa uuid主键生成策略
### 数据库长文本存储，mysql引擎
### spring service层分开实现的方法
类适配器模式与对象适配器模式

抽象类的注入与实现 https://blog.csdn.net/evilcry2012/article/details/78927479

一个接口或抽象类是难以分开成多个方法分别实现的，将其分成多个接口
并继承同一个接口也不行。

### RequestBody注解与ResponseBody注解

如果返回的不是页面，要添加ResponseBody注解。

对于RequestBody注解：
application/x-www-form-urlencoded，可选；
multipart/form-data, 不能处理（即使用@RequestBody不能处理这种格式的数据）；
其他格式，必须（其他格式包括application/json, application/xml等。
这些格式的数据，必须使用@RequestBody来处理）；

### 数据库主从同步与复制
https://www.cnblogs.com/hustcat/archive/2009/12/19/1627525.html
https://www.cnblogs.com/kylinlin/p/5258719.html

主从同步使得数据可以从一个数据库服务器复制到其他服务器上，在复制数据时，
一个服务器充当主服务器（master），其余的服务器充当从服务器（slave）。
因为复制是异步进行的，所以从服务器不需要一直连接着主服务器，从服务器甚至
可以通过拨号断断续续地连接主服务器。通过配置文件，可以指定复制所有的数据库
，某个数据库，甚至是某个数据库上的某个表。

有很多种配置主从同步的方法，可以总结为如下的步骤：

1．在主服务器上，必须开启二进制日志机制和配置一个独立的ID

2．在每一个从服务器上，配置一个唯一的ID，创建一个用来专门复制主服务器数据的账号

3．在开始复制进程前，在主服务器上记录二进制文件的位置信息

4．如果在开始复制之前，数据库中已经有数据，就必须先创建一个数据快照（可以使用
mysqldump导出数据库，或者直接复制数据文件）

5．配置从服务器要连接的主服务器的IP地址和登陆授权，二进制日志文件名和位置

### 加密问题

#### 数据库密码加密
 F:\maven\repository\com\alibaba\druid\1.1.10> java -cp druid-1.1.10.jar com.alibaba.druid.filter.config.ConfigTools 123456

privateKey:MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAnsU5HQBR8H7QhawjHu4rYOQ9mqCzdbN93+ChLRLsOZK9JZhhSw8Q7hyqw4LrK7Uisfjb1PVeyAQxh2rKEpA/CQIDAQABAkAa+6XDOgSy/LpvnVuyrAOPSfr2Ro15WXHxFHoP8QFYn0RqI819LkO4wKbhA43Hu2g6yesIrGE+85/QY62CFbUBAiEA6Rd9P+A8dJxOnsFO3wugIr/qWCne/sze4qJNjdCFznECIQCuX9VbxmXrdFGeMmoiY/8emik567uia9dT2/pkt0d2GQIhANpe7TJoi3rb7TQB+jgwFgg4L/4EzCt+F9nPIEUZ9CGhAiEAoqCaWmeksn3fiQ030y8zxpS8klp6uradMobc9oXAzjECIGTQ76BLr7U1aLWdzET+/VuIagfiflxpCPslFQDnd4oX

publicKey:MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ7FOR0AUfB+0IWsIx7uK2DkPZqgs3Wzfd/goS0S7DmSvSWYYUsPEO4cqsOC6yu1IrH429T1XsgEMYdqyhKQPwkCAwEAAQ==

password:lW2Y36sHhSDFz5QtLsCT57Q/sU3uayv2mKL+DWgk/nwCR3YVJz2lrndolv8XHv0RupBLlumSpkYyEVC4v5pO6A==

## 服务器运维记录

### 2020.1.11 
#1 13：41 安装了jdk，在C:\Program Files\Java，已配置环境变量。
#2 14：38 安装了mysql server，在C:\Program Files\MySQL，用户名root，密码123456，端口3306。
#3 14：38 创建了项目文件夹，在桌面，voluntize
#4 21：02 将项目文件编译后加入服务器，在voluntize\voluntize_jar，项目启动命令：java -jar voluntize.jar
#5 21：06 为了能解压jar包，安装winrar，在C:\Program Files\WinRAR
#6 21：21 远程连接mysql失败，但本地客户端可以连接。经检验，服务器可以ping通，但telnet 3306端口无响应，猜测是防火墙问题
#7 21：23 mysql 建库，create schema voluntize;

### 2020.1.12
#8 00：18 修复bug: 项目软件jdbc连接不上mysql；java.sql.SQLException: Access denied for user 'root'@'localhost' (using password: YES),原因：直接修改jar包中的properties内容时，空格丢失。
#9 00：32 项目部署成功，使用/test接口远程测试其相应，访问无响应，猜测是防火墙问题
#10 00：40 试图将springboot项目注册成为windows后台服务，准备编写windows服务安装程序
#11 01：24 配置服务器防火墙，发现防火墙早已关闭，修改数据库root用户权限：update user set host='%' where user='root';发现仍然不能远程连接，资料显示需要在阿里云服务器控制台中设置端口规则

### 2020.1.19
#12 00：41 更新数据表结构与项目

###2020.1.28
#13 01：53 对表内容补充了部分注释 