**志杰喜欢加渐变 的API文档**


**Description**：chang

**HOST**:localhost:8080

**Contact**:chang

**Version**:1.0

**api url**：/v2/api-docs


# error-api-controller

## 尚未登录


**note**:


**url**:`/noLogin`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name   | description | in     | require | data type | schema |
| ------ | ----------- | ------ | ------- | --------- | ------ |
| ticket | token令牌   | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 关注(目前只做了关注用户)

## 某个用户的粉丝列表


**note**:


**url**:`/api/fans/{userId}`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name    | description            | in     | require | data type | schema |
| ------- | ---------------------- | ------ | ------- | --------- | ------ |
| current | 当前页码               | query  | false   | integer   |        |
| limit   | 单页显示的帖子数量上限 | query  | false   | integer   |        |
| ticket  | token令牌              | header | false   | string    |        |
| userId  | userId                 | path   | true    | integer   |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 关注(目前只做了关注用户)


**note**:


**url**:`/api/follow`


**method**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"entityId": 2,
	"entityType": 3
}
```


**Request Params**：

| name   | description | in     | require | data type    | schema       |
| ------ | ----------- | ------ | ------- | ------------ | ------------ |
| fer    | fer         | body   | true    | 实体请求对象 | 实体请求对象 |
| ticket | token令牌   | header | false   | string       |              |

**schema Description**



**实体请求对象**

| name       | description                                                  | in   | require | data type      | schema |
| ---------- | ------------------------------------------------------------ | ---- | ------- | -------------- | ------ |
| entityId   | 实体ID                                                       | body | false   | integer(int32) |        |
| entityType | 实体类型: 1-帖子(entity:post), 2-评论(entity:comment), 3-用户(entity:user) | body | false   | integer(int32) |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 某个用户的关注列表（人）


**note**:


**url**:`/api/followees/{userId}`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name    | description            | in     | require | data type | schema |
| ------- | ---------------------- | ------ | ------- | --------- | ------ |
| current | 当前页码               | query  | false   | integer   |        |
| limit   | 单页显示的帖子数量上限 | query  | false   | integer   |        |
| ticket  | token令牌              | header | false   | string    |        |
| userId  | userId                 | path   | true    | integer   |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 取消关注(用户)


**note**:


**url**:`/api/unfollow`


**method**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"entityId": 2,
	"entityType": 3
}
```


**Request Params**：

| name   | description | in     | require | data type    | schema       |
| ------ | ----------- | ------ | ------- | ------------ | ------------ |
| etir   | etir        | body   | true    | 实体请求对象 | 实体请求对象 |
| ticket | token令牌   | header | false   | string       |              |

**schema Description**



**实体请求对象**

| name       | description                                                  | in   | require | data type      | schema |
| ---------- | ------------------------------------------------------------ | ---- | ------- | -------------- | ------ |
| entityId   | 实体ID                                                       | body | false   | integer(int32) |        |
| entityType | 实体类型: 1-帖子(entity:post), 2-评论(entity:comment), 3-用户(entity:user) | body | false   | integer(int32) |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 微信applet登录接口

## 获取用户信息与登录状态


**note**:


**url**:`/api/wechat/checkLoginStatus`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**Request Params**：

| name   | description | in     | require | data type | schema |
| ------ | ----------- | ------ | ------- | --------- | ------ |
| ticket | token令牌   | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 微信一键登录接口


**note**:


**url**:`/api/wechat/getUserInfoAndLogin`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"code": ""
}
```


**Request Params**：

| name   | description | in     | require | data type              | schema                 |
| ------ | ----------- | ------ | ------- | ---------------------- | ---------------------- |
| ticket | token令牌   | header | false   | string                 |                        |
| wlcr   | wlcr        | body   | true    | 微信小程序登录请求对象 | 微信小程序登录请求对象 |

**schema Description**



**微信小程序登录请求对象**

| name | description      | in   | require | data type | schema |
| ---- | ---------------- | ---- | ------- | --------- | ------ |
| code | 临时登录凭证code | body | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 搜索API

## 根据关键词搜索


**note**:


**url**:`/api/search`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name    | description            | in     | require | data type | schema |
| ------- | ---------------------- | ------ | ------- | --------- | ------ |
| current | 当前页码               | query  | false   | integer   |        |
| keyword | 关键词                 | query  | false   | string    |        |
| limit   | 单页显示的帖子数量上限 | query  | false   | integer   |        |
| ticket  | token令牌              | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 点赞

## 更改点赞状态，1:已赞，0:未赞，重复请求以取消/重新点赞


**note**:


**url**:`/api/like`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"entityId": 102,
	"entityType": 1,
	"entityUserId": 102,
	"postId": 102
}
```


**Request Params**：

| name   | description | in     | require | data type        | schema           |
| ------ | ----------- | ------ | ------- | ---------------- | ---------------- |
| elr    | elr         | body   | true    | 点赞实体请求对象 | 点赞实体请求对象 |
| ticket | token令牌   | header | false   | string           |                  |

**schema Description**



**点赞实体请求对象**

| name         | description                                                  | in   | require | data type      | schema |
| ------------ | ------------------------------------------------------------ | ---- | ------- | -------------- | ------ |
| entityId     | 实体ID                                                       | body | false   | integer(int32) |        |
| entityType   | 实体类型: 1-帖子(entity:post), 2-评论(entity:comment)        | body | false   | integer(int32) |        |
| entityUserId | 被赞的实体（帖子/评论）的作者ID                              | body | false   | integer(int32) |        |
| postId       | 帖子的 id (点赞了哪个帖子，点赞的评论属于哪个帖子，点赞的回复属于哪个帖子) | body | false   | integer(int32) |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 用户

## 个人评论/回复列表


**note**:


**url**:`/api/user/comment/{userId}`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name    | description            | in     | require | data type | schema |
| ------- | ---------------------- | ------ | ------- | --------- | ------ |
| current | 当前页码               | query  | false   | integer   |        |
| limit   | 单页显示的帖子数量上限 | query  | false   | integer   |        |
| ticket  | token令牌              | header | false   | string    |        |
| userId  | userId                 | path   | true    | integer   |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 进入我的帖子（查询某个用户的帖子列表）


**note**:


**url**:`/api/user/discuss/{userId}`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name    | description            | in     | require | data type | schema |
| ------- | ---------------------- | ------ | ------- | --------- | ------ |
| current | 当前页码               | query  | false   | integer   |        |
| limit   | 单页显示的帖子数量上限 | query  | false   | integer   |        |
| ticket  | token令牌              | header | false   | string    |        |
| userId  | userId                 | path   | true    | integer   |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 还没好-更新图像路径（将本地的图像路径更新为云服务器上的图像路径）


**note**:


**url**:`/api/user/header/url`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"fileName": "这里是文件名"
}
```


**Request Params**：

| name   | description | in     | require | data type      | schema         |
| ------ | ----------- | ------ | ------- | -------------- | -------------- |
| fnr    | fnr         | body   | true    | 文件名请求对象 | 文件名请求对象 |
| ticket | token令牌   | header | false   | string         |                |

**schema Description**



**文件名请求对象**

| name     | description | in   | require | data type | schema |
| -------- | ----------- | ---- | ------- | --------- | ------ |
| fileName | 文件名      | body | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 修改用户密码


**note**:


**url**:`/api/user/password`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**Request Params**：

| name   | description | in     | require | data type | schema |
| ------ | ----------- | ------ | ------- | --------- | ------ |
| newPwd | 新密码      | query  | false   | string    |        |
| oldPwd | 旧密码      | query  | false   | string    |        |
| ticket | token令牌   | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 个人主页（个人数据）


**note**:


**url**:`/api/user/profile/{userId}`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name   | description | in     | require | data type | schema |
| ------ | ----------- | ------ | ------- | --------- | ------ |
| ticket | token令牌   | header | false   | string    |        |
| userId | userId      | path   | true    | integer   |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 账号设置信息


**note**:


**url**:`/api/user/setting`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name   | description | in     | require | data type | schema |
| ------ | ----------- | ------ | ------- | --------- | ------ |
| ticket | token令牌   | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 登录、登出、注册、验证码API

## getKaptcha


**note**:


**url**:`/api/kaptcha`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name   | description | in     | require | data type | schema |
| ------ | ----------- | ------ | ------- | --------- | ------ |
| ticket | token令牌   | header | false   | string    |        |

**Response Example**:

```json

```

**Response Params**:


No data





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## login


**note**:


**url**:`/api/login`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**Request Params**：

| name         | description  | in     | require | data type        | schema           |
| ------------ | ------------ | ------ | ------- | ---------------- | ---------------- |
| kaptchaOwner | kaptchaOwner | body   | false   | string           |                  |
| ticket       | token令牌    | header | false   | string           |                  |
| user         | user         | body   | true    | 用户登录请求对象 | 用户登录请求对象 |

**schema Description**



**用户登录请求对象**

| name       | description | in   | require | data type | schema |
| ---------- | ----------- | ---- | ------- | --------- | ------ |
| code       | 验证码      | body | false   | string    |        |
| password   | 密码        | body | false   | string    |        |
| rememberMe | 记住我      | body | false   | boolean   |        |
| username   | 用户名      | body | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## logout


**note**:


**url**:`/api/logout`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name   | description | in   | require | data type | schema |
| ------ | ----------- | ---- | ------- | --------- | ------ |
| ticket | ticket      | body | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## register


**note**:


**url**:`/api/register`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"email": "8anbang@gmail.com",
	"password": "shijian123",
	"username": "zhijie"
}
```


**Request Params**：

| name   | description | in     | require | data type | schema |
| ------ | ----------- | ------ | ------- | --------- | ------ |
| ticket | token令牌   | header | false   | string    |        |
| user   | user        | body   | true    | 用户      | 用户   |

**schema Description**



**用户**

| name     | description                            | in   | require | data type | schema |
| -------- | -------------------------------------- | ---- | ------- | --------- | ------ |
| email    | 邮箱，用于接受激活邮件以及后续找回密码 | body | false   | string    |        |
| password | 用户密码                               | body | true    | string    |        |
| username | 用户名                                 | body | true    | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## resetPwd


**note**:


**url**:`/api/resetPwd`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**Request Params**：

| name            | description     | in     | require | data type | schema |
| --------------- | --------------- | ------ | ------- | --------- | ------ |
| emailVerifyCode | emailVerifyCode | query  | true    | string    |        |
| kaptchaCode     | kaptchaCode     | query  | true    | string    |        |
| kaptchaOwner    | kaptchaOwner    | body   | false   | string    |        |
| password        | password        | query  | true    | string    |        |
| ticket          | token令牌       | header | false   | string    |        |
| username        | username        | query  | true    | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## sendEmailCodeForResetPwd


**note**:


**url**:`/api/sendEmailCodeForResetPwd`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**Request Params**：

| name         | description  | in     | require | data type | schema |
| ------------ | ------------ | ------ | ------- | --------- | ------ |
| kaptcha      | kaptcha      | query  | true    | string    |        |
| kaptchaOwner | kaptchaOwner | body   | false   | string    |        |
| ticket       | token令牌    | header | false   | string    |        |
| username     | username     | query  | true    | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 私信、系统通知

## 私信详情


**note**:


**url**:`/api/letter/detail/{conversationId}`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name           | description            | in     | require | data type | schema |
| -------------- | ---------------------- | ------ | ------- | --------- | ------ |
| conversationId | 会话id，示例：‘2_118’  | path   | false   | string    |        |
| current        | 当前页码               | query  | false   | integer   |        |
| limit          | 单页显示的帖子数量上限 | query  | false   | integer   |        |
| ticket         | token令牌              | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 私信列表


**note**:


**url**:`/api/letter/list`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name    | description            | in     | require | data type | schema |
| ------- | ---------------------- | ------ | ------- | --------- | ------ |
| current | 当前页码               | query  | false   | integer   |        |
| limit   | 单页显示的帖子数量上限 | query  | false   | integer   |        |
| ticket  | token令牌              | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 发送私信


**note**:


**url**:`/api/letter/send`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"content": "这是一条发给用户admin的测试信息",
	"toName": "admin"
}
```


**Request Params**：

| name   | description | in     | require | data type                    | schema                       |
| ------ | ----------- | ------ | ------- | ---------------------------- | ---------------------------- |
| slnc   | slnc        | body   | true    | 发送私信：目标与内容请求对象 | 发送私信：目标与内容请求对象 |
| ticket | token令牌   | header | false   | string                       |                              |

**schema Description**



**发送私信：目标与内容请求对象**

| name    | description | in   | require | data type | schema |
| ------- | ----------- | ---- | ------- | --------- | ------ |
| content | 私信内容    | body | false   | string    |        |
| toName  | 私信目标    | body | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 查询某个主题（关注follow/赞like/评论comment）所包含的通知列表


**note**:


**url**:`/api/notice/detail/{topic}`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name    | description            | in     | require | data type | schema |
| ------- | ---------------------- | ------ | ------- | --------- | ------ |
| current | 当前页码               | query  | false   | integer   |        |
| limit   | 单页显示的帖子数量上限 | query  | false   | integer   |        |
| ticket  | token令牌              | header | false   | string    |        |
| topic   | topic                  | path   | true    | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 通知列表（只显示最新一条消息）


**note**:


**url**:`/api/notice/list`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name   | description | in     | require | data type | schema |
| ------ | ----------- | ------ | ------- | --------- | ------ |
| ticket | token令牌   | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 网站数据（admin、moderator only）


## 统计网站 DAU


**note**:


**url**:`/api/data/dau`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"end": "20231020",
	"start": "20221020"
}
```


**Request Params**：

| name   | description | in     | require | data type        | schema           |
| ------ | ----------- | ------ | ------- | ---------------- | ---------------- |
| dser   | dser        | body   | true    | 起末日期请求对象 | 起末日期请求对象 |
| ticket | token令牌   | header | false   | string           |                  |

**schema Description**



**起末日期请求对象**

| name  | description | in   | require | data type | schema |
| ----- | ----------- | ---- | ------- | --------- | ------ |
| end   | 结束日期    | body | false   | string    |        |
| start | 开始日期    | body | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 统计网站 uv


**note**:


**url**:`/api/data/uv`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"end": "20231020",
	"start": "20221020"
}
```


**Request Params**：

| name   | description | in     | require | data type        | schema           |
| ------ | ----------- | ------ | ------- | ---------------- | ---------------- |
| dser   | dser        | body   | true    | 起末日期请求对象 | 起末日期请求对象 |
| ticket | token令牌   | header | false   | string           |                  |

**schema Description**



**起末日期请求对象**

| name  | description | in   | require | data type | schema |
| ----- | ----------- | ---- | ------- | --------- | ------ |
| end   | 结束日期    | body | false   | string    |        |
| start | 开始日期    | body | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 评论/回复

## addComment


**note**:


**url**:`/api/comment/add/{discussPostId}`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"content": "再回一个",
	"entityId": 1,
	"entityType": 2,
	"targetId": 102
}
```


**Request Params**：

| name          | description   | in     | require | data type | schema |
| ------------- | ------------- | ------ | ------- | --------- | ------ |
| comment       | comment       | body   | true    | 评论      | 评论   |
| discussPostId | discussPostId | path   | true    | integer   |        |
| ticket        | token令牌     | header | false   | string    |        |

**schema Description**



**评论**

| name       | description                      | in   | require | data type      | schema |
| ---------- | -------------------------------- | ---- | ------- | -------------- | ------ |
| content    | 内容                             | body | false   | string         |        |
| entityId   | 评论目标的ID（帖子ID/评论ID）    | body | false   | integer(int32) |        |
| entityType | 评论目标的类型（1-帖子、2-评论） | body | false   | integer(int32) |        |
| targetId   | 指明对哪个用户进行评论(用户id)   | body | false   | integer(int32) |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
# 通知（讨论）API
## 添加讨论（发布通知） (auth)


**note**:


**url**:`/api/discuss/add`


**method**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"content": "",
	"title": ""
}
```


**Request Params**：

| name      | description | in     | require | data type              | schema                 |
| --------- | ----------- | ------ | ------- | ---------------------- | ---------------------- |
| dpReceive | dpReceive   | body   | true    | 讨论标题、内容请求对象 | 讨论标题、内容请求对象 |
| ticket    | token令牌   | header | false   | string                 |                        |

**schema Description**



**讨论标题、内容请求对象**

| name    | description | in   | require | data type | schema |
| ------- | ----------- | ---- | ------- | --------- | ------ |
| content | 内容        | body | false   | string    |        |
| title   | 标题，非空  | body | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 删除讨论 (auth admin-1 only)


**note**:


**url**:`/api/discuss/delete`


**method**：`DELETE`


**consumes**:``


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"id": 20
}
```


**Request Params**：

| name          | description   | in     | require | data type      | schema         |
| ------------- | ------------- | ------ | ------- | -------------- | -------------- |
| discussPostId | discussPostId | body   | true    | 讨论ID请求对象 | 讨论ID请求对象 |
| ticket        | token令牌     | header | false   | string         |                |

**schema Description**



**讨论ID请求对象**

| name | description | in   | require | data type      | schema |
| ---- | ----------- | ---- | ------- | -------------- | ------ |
| id   | 帖子的id    | body | false   | integer(int32) |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 204  | No Content   |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
## 获取所有讨论


**note**:


**url**:`/api/discuss/detail/all`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name      | description                              | in     | require | data type | schema |
| --------- | ---------------------------------------- | ------ | ------- | --------- | ------ |
| current   | 当前页码                                 | query  | false   | integer   |        |
| limit     | 单页显示的帖子数量上限                   | query  | false   | integer   |        |
| orderMode | 默认是 0--按时间排序，可选 1--按分值排序 | query  | false   | integer   |        |
| ticket    | token令牌                                | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 进入特定讨论详情页


**note**:


**url**:`/api/discuss/detail/{discussPostId}`


**method**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**Request Params**：

| name          | description            | in     | require | data type | schema |
| ------------- | ---------------------- | ------ | ------- | --------- | ------ |
| current       | 当前页码               | query  | false   | integer   |        |
| discussPostId | 单条讨论的ID           | path   | false   | integer   |        |
| limit         | 单页显示的帖子数量上限 | query  | false   | integer   |        |
| ticket        | token令牌              | header | false   | string    |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 置顶讨论 (auth master-2 only)


**note**:


**url**:`/api/discuss/top`


**method**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"id": 20,
	"type": 0
}
```


**Request Params**：

| name      | description | in     | require | data type            | schema               |
| --------- | ----------- | ------ | ------- | -------------------- | -------------------- |
| idAndType | idAndType   | body   | true    | 讨论ID、状态请求对象 | 讨论ID、状态请求对象 |
| ticket    | token令牌   | header | false   | string               |                      |

**schema Description**



**讨论ID、状态请求对象**

| name | description          | in   | require | data type      | schema |
| ---- | -------------------- | ---- | ------- | -------------- | ------ |
| id   | 帖子的id             | body | false   | integer(int32) |        |
| type | 状态：0-普通，1-置顶 | body | false   | integer(int32) |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 未完善！图片上传 (auth)


**note**:


**url**:`/api/discuss/uploadMdPic`


**method**：`POST`


**consumes**:`["multipart/form-data"]`


**produces**:`["*/*"]`



**Request Params**：

| name                | description         | in       | require | data type | schema |
| ------------------- | ------------------- | -------- | ------- | --------- | ------ |
| editormd-image-file | editormd-image-file | formData | false   | file      |        |
| ticket              | token令牌           | header   | false   | string    |        |

**Response Example**:

```json

```

**Response Params**:


No data





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           |        |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |
## 加精讨论 (auth master-2 only)


**note**:


**url**:`/api/discuss/wonderful`


**method**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**Request Example**：
```json
{
	"id": 20
}
```


**Request Params**：

| name   | description | in     | require | data type      | schema         |
| ------ | ----------- | ------ | ------- | -------------- | -------------- |
| dpId   | dpId        | body   | true    | 讨论ID请求对象 | 讨论ID请求对象 |
| ticket | token令牌   | header | false   | string         |                |

**schema Description**



**讨论ID请求对象**

| name | description | in   | require | data type      | schema |
| ---- | ----------- | ---- | ------- | -------------- | ------ |
| id   | 帖子的id    | body | false   | integer(int32) |        |

**Response Example**:

```json
{
	"code": 0,
	"data": {},
	"message": "",
	"success": true
}
```

**Response Params**:


| name    | description | type           | schema         |
| ------- | ----------- | -------------- | -------------- |
| code    |             | integer(int32) | integer(int32) |
| data    |             | object         |                |
| message |             | string         |                |
| success |             | boolean        |                |





**Response Status**:


| code | description  | schema |
| ---- | ------------ | ------ |
| 200  | OK           | R      |
| 201  | Created      |        |
| 401  | Unauthorized |        |
| 403  | Forbidden    |        |
| 404  | Not Found    |        |