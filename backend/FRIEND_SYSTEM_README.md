# 好友系统 (Friend System)

这是一个完整的Spring Boot好友系统，支持Google OAuth登录，包含用户管理、好友关系和好友请求功能。

## 数据库表结构

### 1. users 表
- `id`: 主键
- `username`: 用户名（唯一）
- `email`: 邮箱（唯一）
- `display_name`: 显示名称
- `avatar_url`: 头像URL
- `phone_number`: 电话号码
- `created_at`: 创建时间
- `updated_at`: 更新时间
- `is_online`: 是否在线
- `last_seen`: 最后在线时间
- `google_id`: Google用户ID（唯一）
- `auth_provider`: 认证提供商（默认"GOOGLE"）
- `email_verified`: 邮箱是否已验证（Google用户默认为true）

### 2. friends 表
- `id`: 主键
- `user_id`: 用户ID（外键）
- `friend_id`: 好友ID（外键）
- `created_at`: 成为好友的时间
- `nickname`: 好友昵称
- `is_favorite`: 是否收藏
- `last_interaction`: 最后互动时间

### 3. friend_requests 表
- `id`: 主键
- `sender_id`: 发送者ID（外键）
- `receiver_id`: 接收者ID（外键）
- `status`: 请求状态（PENDING, ACCEPTED, REJECTED, CANCELLED）
- `message`: 请求消息
- `created_at`: 创建时间
- `updated_at`: 更新时间

## API 端点

### 用户管理 (User Management)
```
GET    /api/users                    # 获取所有用户
GET    /api/users/{id}               # 根据ID获取用户
GET    /api/users/username/{username} # 根据用户名获取用户
GET    /api/users/email/{email}      # 根据邮箱获取用户
GET    /api/users/google/{googleId}  # 根据Google ID获取用户
GET    /api/users/search?q={term}    # 搜索用户
GET    /api/users/{userId}/not-friends # 获取非好友用户列表
POST   /api/users                    # 创建用户
POST   /api/users/google-login       # Google登录/注册
PUT    /api/users/{id}               # 更新用户
DELETE /api/users/{id}               # 删除用户
GET    /api/users/check/username/{username} # 检查用户名是否存在
GET    /api/users/check/email/{email}       # 检查邮箱是否存在
GET    /api/users/check/phone/{phoneNumber} # 检查电话号码是否存在
GET    /api/users/check/google/{googleId}   # 检查Google ID是否存在
```

### 好友管理 (Friend Management)
```
GET    /api/friends/{userId}                    # 获取用户的好友列表
GET    /api/friends/{userId}/users              # 获取好友用户信息
GET    /api/friends/{userId}/favorites          # 获取收藏的好友
GET    /api/friends/{userId}/{friendId}         # 获取特定好友关系
GET    /api/friends/{userId}/{friendId}/check   # 检查是否为好友
POST   /api/friends/{userId}/{friendId}         # 添加好友
DELETE /api/friends/{userId}/{friendId}         # 删除好友
PUT    /api/friends/{userId}/{friendId}/nickname?nickname={name} # 设置好友昵称
PUT    /api/friends/{userId}/{friendId}/favorite # 切换收藏状态
PUT    /api/friends/{userId}/{friendId}/interaction # 更新最后互动时间
```

### 好友请求 (Friend Requests)
```
GET    /api/friend-requests/received/{receiverId} # 获取收到的待处理请求
GET    /api/friend-requests/sent/{senderId}       # 获取发送的待处理请求
GET    /api/friend-requests/{id}                  # 获取特定请求
POST   /api/friend-requests/{senderId}/{receiverId}?message={msg} # 发送好友请求
PUT    /api/friend-requests/{requestId}/accept    # 接受好友请求
PUT    /api/friend-requests/{requestId}/reject    # 拒绝好友请求
PUT    /api/friend-requests/{requestId}/cancel?userId={id} # 取消请求
DELETE /api/friend-requests/{requestId}           # 删除请求
GET    /api/friend-requests/{userId}/{friendId}/history # 获取请求历史
GET    /api/friend-requests/{userId}/{friendId}/pending # 检查是否有待处理请求
```

## Google登录流程

### 1. 前端Google登录
```javascript
import { GoogleSignin } from '@react-native-google-signin/google-signin';

const handleGoogleLogin = async () => {
  try {
    await GoogleSignin.hasPlayServices();
    const userInfo = await GoogleSignin.signIn();
    
    // 发送用户信息到后端
    const response = await fetch('http://localhost:5050/api/users/google-login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        googleId: userInfo.user.id,
        email: userInfo.user.email,
        displayName: userInfo.user.name,
        avatarUrl: userInfo.user.photo
      })
    });
    
    const user = await response.json();
    // 保存用户信息到本地存储
    // 导航到主页面
  } catch (error) {
    console.error('Google login failed:', error);
  }
};
```

### 2. 后端处理
- 检查Google ID是否已存在
- 如果存在，更新用户信息并设置在线状态
- 如果不存在，创建新用户
- 返回用户信息（包含数据库ID）

## 使用流程

### 1. 添加好友流程
1. 用户A通过Google登录获取用户信息
2. 用户A搜索用户B
3. 用户A发送好友请求给用户B
4. 用户B收到好友请求通知
5. 用户B可以选择接受或拒绝请求
6. 如果接受，双方自动成为好友

### 2. 好友管理功能
- 查看好友列表
- 设置好友昵称
- 收藏/取消收藏好友
- 删除好友
- 查看好友在线状态

### 3. 好友请求管理
- 发送好友请求
- 查看收到的请求
- 查看发送的请求
- 接受/拒绝请求
- 取消已发送的请求

## 测试数据

系统启动时会自动创建以下测试用户：
- Alice Johnson (alice@example.com) - Google ID: test_google_id_1
- Bob Smith (bob@example.com) - Google ID: test_google_id_2
- Charlie Brown (charlie@example.com) - Google ID: test_google_id_3
- Diana Prince (diana@example.com) - Google ID: test_google_id_4

## 启动应用

1. 确保PostgreSQL数据库正在运行
2. 确保数据库 `aichat` 已创建
3. 运行Spring Boot应用：
   ```bash
   mvn spring-boot:run
   ```
4. 应用将在 `http://localhost:5050` 启动
5. API文档可在 `http://localhost:5050/swagger-ui.html` 查看

## 前端集成

在React Native应用中，你可以使用这些API端点来：
- 处理Google登录并创建/更新用户
- 在Contacts页面显示所有用户
- 发送好友请求
- 管理好友列表
- 处理好友请求通知

### Google登录示例
```javascript
// Google登录并创建/更新用户
const googleLogin = async (googleUserInfo) => {
  const response = await fetch('http://localhost:5050/api/users/google-login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      googleId: googleUserInfo.user.id,
      email: googleUserInfo.user.email,
      displayName: googleUserInfo.user.name,
      avatarUrl: googleUserInfo.user.photo
    })
  });
  return response.json();
};

// 搜索用户
const searchUsers = async (searchTerm) => {
  const response = await fetch(`http://localhost:5050/api/users/search?q=${searchTerm}`);
  return response.json();
};

// 发送好友请求
const sendFriendRequest = async (senderId, receiverId, message) => {
  const response = await fetch(`http://localhost:5050/api/friend-requests/${senderId}/${receiverId}?message=${message}`, {
    method: 'POST'
  });
  return response.json();
};

// 获取好友列表
const getFriends = async (userId) => {
  const response = await fetch(`http://localhost:5050/api/friends/${userId}`);
  return response.json();
};
```

## 安全考虑

1. **无密码存储**: 由于使用Google OAuth，不需要存储用户密码
2. **邮箱验证**: Google用户的邮箱自动标记为已验证
3. **唯一标识**: 使用Google ID作为唯一标识符
4. **CORS配置**: 已配置跨域访问支持前端开发 