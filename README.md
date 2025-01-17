langjie-stream-backend

| 状态码 | 描述                                     |
| ------ | ---------------------------------------- |
| 10001  | 无效token                                |
| 10002  | 用户不存在                               |
| 10003  | 没有权限                                 |
| 10004  | 非法请求                                 |
| 10005  | token已过期                              |
| 10006  | 拦截器其他错误                           |
| 11000  | 用户注册成功                             |
| 11001  | 用户登录成功                             |
| 12000  | 用户注册失败，用户名不可用               |
| 12001  | 用户注册失败，其他原因                   |
| 12002  | 用户登陆失败，用户名不存在               |
| 12003  | 用户登陆失败，密码错误                   |
| 12100  | 用户查询某个用户信息成功                 |
| 12101  | 用户查询某个人用户信息失败：该用户不存在 |
| 12102  | 用户查询某个人用户信息失败：其他错误     |
| 13000  | 添加直播间成功                           |
| 13001  | 添加直播间失败，直播间名已存在           |
| 13002  | 添加直播间失败，其他原因                 |
| 13100  | 进入直播间申请通过                       |
| 13101  | 进入直播申请未通过，密码错误             |
| 13102  | 进入直播间申请未通过，其他错误           |
| 13200  | 获取直播间信息成功                       |
| 13201  | 获取直播间信息失败：无认证信息           |
| 13202  | 获取直播间信息失败：其他原因             |
| 13210  | 获取所有直播间信息成功                   |
| 13211  | 获取所有直播间信息失败                   |
| 13220  | 获取所有直播间类型成功                   |
| 13221  | 获取所有直播间类型失败                   |
| 13230  | 获取所有奖品类型成功                     |
| 13231  | 获取所有奖品类型失败                     |
| 13240  | 添加新奖品成功                           |
| 13241  | 添加新奖品失败                           |
| 13242  | 删除奖品成功                             |
| 13243  | 删除奖品失败                             |
| 13244  | 抽奖成功                                 |
| 13245  | 抽奖失败                                 |
| 13250  | 获取直播间所有奖品成功                   |
| 13251  | 获取直播间所有奖品失败                   |
| 13260  | 获取某直播间所有中奖记录成功             |
| 13260  | 获取某直播间所有中奖记录失败             |
| 13300  | 获取直播间观众列表成功                   |
| 13301  | 获取直播间观众列表失败                   |
| 13310  | 获取所有直播间创建者成功                 |
| 13311  | 获取所有直播间创建者失败                 |
| 13320  | 用户获取所有自己创建的直播间成功         |
| 13321  | 用户获取所有自己创建的直播间失败         |
| 13330  | 用户修改自己的直播间信息成功             |
| 13331  | 用户修改自己的直播间信息失败             |
| 13340  | 用户删除自己的直播间成功                 |
| 13341  | 用户删除自己的直播间失败                 |

| 客户端 -> 服务端 消息前缀 | 消息说明                         |
| ------------------------- | -------------------------------- |
| ROOM_MSG                  | 直播间消息，仅广播给该直播间用户 |
| BROADCAST_MSG             | 广播消息，广播给所有人           |
|                           |                                  |

| 服务端 -> 客户端 消息                                        | 说明     |
| ------------------------------------------------------------ | -------- |
| {msg: "", msgType: "", sendTime: "", senderId: "", senderName: ""} | 顾名思义 |
|                                                              |          |
|                                                              |          |



Redis存储数据结构

| key                                                        | value                                                        | 数据类型 | 说明                                                         |
| ---------------------------------------------------------- | ------------------------------------------------------------ | -------- | ------------------------------------------------------------ |
| langjie_stream:user_info:{user_id}                         | 用户的数据，json序列化                                       | string   | 缓存用户数据                                                 |
|                                                            |                                                              |          |                                                              |
| langjie_stream:live_room_viewer_status:{live_id}:{user_id} | ALLOW                                                        | string   | 表示某个用户进入某个直播间的状态                             |
| langjie_stream:live_room_viewer_status:{live_id}:{user_id} | HOLD                                                         | string   | 这种情况是当用户断开连接后，服务器会暂存认证信息5s，这样用户刷新页面，只要在5s内页面重新加载，则可以直接连接 |
|                                                            |                                                              |          |                                                              |
| langjie_stream:live_room_chat_history:{live_id}            | element: 发送的消息对象序列化的字符串，score: 该消息发送的时间 | zset     | zset会自动对历史消息进行排序，需要在后端上一个定时任务来定时清除过期的消息 |
|                                                            |                                                              |          |                                                              |

注：

1. lombok的@NoArgsConstructor会在mybatisplus的查询映射时冲突，在PO中不允许使用@NoArgsConstructor
1. 其实rtmp-module可以只有一个application，在推拉多个流时给不同的流设置不同的串流密钥即可