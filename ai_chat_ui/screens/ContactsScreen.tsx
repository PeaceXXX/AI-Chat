import React, { useState, useEffect } from 'react';
import { SafeAreaView, StatusBar, Text, View, StyleSheet, TextInput, TouchableOpacity, FlatList, Modal, Pressable } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';

declare global {
  var chatFriends: string[];
}
if (!global.chatFriends) global.chatFriends = [];

const FRIENDS_KEY = 'contacts_friends_list';
const CHAT_FRIENDS_KEY = 'chat_friends_list';

export default function ContactsScreen() {
  const [friendName, setFriendName] = useState('');
  const [friends, setFriends] = useState<string[]>([]);
  const [selectedFriend, setSelectedFriend] = useState<string | null>(null);
  const [showMenu, setShowMenu] = useState(false);
  const navigation = useNavigation<any>();

  // 加载好友列表
  useEffect(() => {
    (async () => {
      const data = await AsyncStorage.getItem(FRIENDS_KEY);
      if (data) setFriends(JSON.parse(data));
    })();
  }, []);

  // 保存好友列表
  useEffect(() => {
    AsyncStorage.setItem(FRIENDS_KEY, JSON.stringify(friends));
  }, [friends]);

  const handleAddFriend = () => {
    if (friendName.trim()) {
      setFriends(prev => [...prev, friendName.trim()]);
      setFriendName('');
    }
  };

  const handleFriendPress = (name: string) => {
    setSelectedFriend(name);
    setShowMenu(true);
  };

  const handleChat = () => {
    setShowMenu(false);
    if (selectedFriend) {
      // 聊天过的好友加入全局列表
      if (!global.chatFriends.includes(selectedFriend)) {
        global.chatFriends.push(selectedFriend);
      }
      navigation.navigate('Chat', { friendName: selectedFriend });
    }
  };

  const handleDelete = async () => {
    setShowMenu(false);
    if (selectedFriend) {
      setFriends(prev => prev.filter(f => f !== selectedFriend));
      global.chatFriends = global.chatFriends.filter((f: string) => f !== selectedFriend);
      // 更新持久化聊天好友列表
      await AsyncStorage.setItem(CHAT_FRIENDS_KEY, JSON.stringify(global.chatFriends));
      if (typeof (global as any).onChatFriendsChange === 'function') {
        (global as any).onChatFriendsChange();
      }
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#ffffff" />
      <View style={styles.content}>
        <Text style={styles.title}>Contacts</Text>
        <View style={styles.inputRow}>
          <TextInput
            style={styles.input}
            placeholder="输入好友名字"
            value={friendName}
            onChangeText={setFriendName}
          />
          <TouchableOpacity style={styles.addButton} onPress={handleAddFriend}>
            <Text style={styles.addButtonText}>添加</Text>
          </TouchableOpacity>
        </View>
        <FlatList
          data={friends}
          keyExtractor={(item, idx) => item + idx}
          renderItem={({ item }) => (
            <TouchableOpacity style={styles.friendItem} onPress={() => handleFriendPress(item)}>
              <Text style={styles.friendName}>{item}</Text>
            </TouchableOpacity>
          )}
          ListEmptyComponent={<Text style={styles.emptyText}>暂无好友</Text>}
        />
      </View>
      <Modal visible={showMenu} transparent animationType="fade">
        <Pressable style={styles.modalOverlay} onPress={() => setShowMenu(false)}>
          <View style={styles.menuBox}>
            <TouchableOpacity style={styles.menuItem} onPress={handleChat}>
              <Text style={styles.menuText}>Chat</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.menuItem} onPress={handleDelete}>
              <Text style={[styles.menuText, { color: 'red' }]}>Delete</Text>
            </TouchableOpacity>
          </View>
        </Pressable>
      </Modal>
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
  inputRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 20,
  },
  input: {
    flex: 1,
    height: 40,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 10,
    marginRight: 10,
    fontSize: 16,
  },
  addButton: {
    backgroundColor: '#3498db',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
  },
  addButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
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
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.2)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  menuBox: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 20,
    width: 180,
    elevation: 8,
    alignItems: 'center',
  },
  menuItem: {
    paddingVertical: 12,
    width: '100%',
    alignItems: 'center',
  },
  menuText: {
    fontSize: 18,
  },
}); 