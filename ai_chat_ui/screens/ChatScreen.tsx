import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useState, useEffect, useRef } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, TextInput, FlatList, KeyboardAvoidingView, Platform } from 'react-native';

interface Message {
  message: string;
  datetime: string;
  from: 'me' | 'friend';
}

function formatTime(datetime: string) {
  const d = new Date(datetime);
  return `${d.getFullYear()}-${d.getMonth() + 1}-${d.getDate()} ${d.getHours()}:${d.getMinutes().toString().padStart(2, '0')}`;
}

const CHAT_FRIENDS_KEY = 'chat_friends_list';

export default function ChatScreen({ route, navigation }: any) {
  const { friendName } = route.params;
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState<Message[]>([]);
  const flatListRef = useRef<FlatList>(null);

  // 加载历史消息
  useEffect(() => {
    (async () => {
      const key = `chat_${friendName}`;
      const data = await AsyncStorage.getItem(key);
      if (data) setMessages(JSON.parse(data));
    })();
  }, [friendName]);

  // 保存消息
  useEffect(() => {
    const key = `chat_${friendName}`;
    AsyncStorage.setItem(key, JSON.stringify(messages));
  }, [messages, friendName]);

  // 发送消息
  const handleSend = async () => {
    if (input.trim()) {
      const newMsg: Message = { message: input.trim(), datetime: new Date().toISOString(), from: 'me' };
      setMessages(prev => [...prev, newMsg]);
      setInput('');
      // 新增：自动加入聊天好友列表并持久化
      let chatFriends: string[] = [];
      const data = await AsyncStorage.getItem(CHAT_FRIENDS_KEY);
      if (data) chatFriends = JSON.parse(data);
      if (!chatFriends.includes(friendName)) {
        chatFriends.push(friendName);
        await AsyncStorage.setItem(CHAT_FRIENDS_KEY, JSON.stringify(chatFriends));
        global.chatFriends = chatFriends;
        if (typeof (global as any).onChatFriendsChange === 'function') {
          (global as any).onChatFriendsChange();
        }
      }
      // 模拟朋友回复
      setTimeout(() => {
        const reply: Message = { message: '收到: ' + newMsg.message, datetime: new Date().toISOString(), from: 'friend' };
        setMessages(prev => [...prev, reply]);
      }, 800);
    }
  };

  // 自动滚动到底部
  useEffect(() => {
    if (flatListRef.current && messages.length > 0) {
      flatListRef.current.scrollToEnd({ animated: true });
    }
  }, [messages]);

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.navigate('MainTabs', { screen: 'Chats' })} style={styles.backButton}>
          <Text style={styles.backButtonText}>{'< 返回'}</Text>
        </TouchableOpacity>
        <Text style={styles.headerTitle}>{friendName}</Text>
      </View>
      <KeyboardAvoidingView style={styles.content} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <FlatList
          ref={flatListRef}
          data={messages}
          keyExtractor={(_, idx) => idx.toString()}
          renderItem={({ item }) => (
            <View style={[styles.messageItem, item.from === 'me' ? styles.me : styles.friend]}>
              <Text style={styles.messageText}>{item.message}</Text>
              <Text style={styles.timeText}>{formatTime(item.datetime)}</Text>
            </View>
          )}
          ListEmptyComponent={<Text style={styles.emptyText}>暂无消息</Text>}
        />
      </KeyboardAvoidingView>
      <View style={styles.inputRow}>
        <TextInput
          style={styles.input}
          placeholder="输入消息"
          value={input}
          onChangeText={setInput}
        />
        <TouchableOpacity style={styles.sendButton} onPress={handleSend}>
          <Text style={styles.sendButtonText}>发送</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#fff' },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingTop: 40,
    paddingBottom: 10,
    paddingHorizontal: 10,
    backgroundColor: '#3498db',
  },
  backButton: {
    padding: 8,
    marginRight: 10,
  },
  backButtonText: {
    color: '#fff',
    fontSize: 18,
  },
  headerTitle: {
    color: '#fff',
    fontSize: 20,
    fontWeight: 'bold',
  },
  content: {
    flex: 1,
    padding: 16,
  },
  messageItem: {
    marginBottom: 12,
    borderRadius: 8,
    padding: 10,
    maxWidth: '80%',
  },
  me: {
    alignSelf: 'flex-end',
    backgroundColor: '#e1f5fe',
  },
  friend: {
    alignSelf: 'flex-start',
    backgroundColor: '#f1f8e9',
  },
  messageText: {
    fontSize: 16,
    color: '#2c3e50',
  },
  timeText: {
    fontSize: 12,
    color: '#888',
    marginTop: 4,
    textAlign: 'right',
  },
  emptyText: {
    color: '#aaa',
    fontSize: 16,
    marginTop: 30,
    textAlign: 'center',
  },
  inputRow: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 10,
    borderTopColor: '#eee',
    borderTopWidth: 1,
    backgroundColor: '#fafafa',
  },
  input: {
    flex: 1,
    height: 40,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 10,
    fontSize: 16,
    marginRight: 10,
    backgroundColor: '#fff',
  },
  sendButton: {
    backgroundColor: '#3498db',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
  },
  sendButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
}); 