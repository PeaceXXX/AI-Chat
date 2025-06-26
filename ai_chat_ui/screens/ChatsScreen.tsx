import React, { useState, useEffect } from 'react';
import { SafeAreaView, StatusBar, Text, View, StyleSheet, FlatList, TouchableOpacity } from 'react-native';

declare global {
  var chatFriends: string[];
}

export default function ChatsScreen({ navigation }: any) {
  const [chatFriends, setChatFriends] = useState<string[]>(global.chatFriends || []);

  useEffect(() => {
    const handler = () => setChatFriends([...global.chatFriends]);
    // 简单事件模拟
    (global as any).onChatFriendsChange = handler;
    return () => { (global as any).onChatFriendsChange = undefined; };
  }, []);

  // 监听全局 chatFriends 变化
  useEffect(() => {
    setChatFriends([...global.chatFriends]);
  }, [global.chatFriends.length]);

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#ffffff" />
      <View style={styles.content}>
        <Text style={styles.title}>AI chats</Text>
        <FlatList
          data={chatFriends}
          keyExtractor={(item, idx) => item + idx}
          renderItem={({ item }) => (
            <TouchableOpacity style={styles.friendItem} onPress={() => navigation.navigate('Chat', { friendName: item })}>
              <Text style={styles.friendName}>{item}</Text>
            </TouchableOpacity>
          )}
          ListEmptyComponent={<Text style={styles.emptyText}>暂无聊天记录</Text>}
        />
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#ffffff',
  },
  content: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    padding: 20,
  },
  title: {
    fontSize: 36,
    fontWeight: 'bold',
    color: '#2c3e50',
    marginBottom: 20,
    textAlign: 'center',
  },
  friendItem: {
    padding: 12,
    borderBottomColor: '#eee',
    borderBottomWidth: 1,
    width: 300,
  },
  friendName: {
    fontSize: 18,
    color: '#2c3e50',
  },
  emptyText: {
    color: '#aaa',
    fontSize: 16,
    marginTop: 30,
    textAlign: 'center',
  },
  backButton: {
    position: 'absolute',
    top: 20,
    left: 20,
    padding: 10,
  },
  backButtonText: {
    fontSize: 18,
    color: '#2c3e50',
  },
}); 