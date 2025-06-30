import React, { useEffect } from 'react';
import { Alert, SafeAreaView, StatusBar, Text, TouchableOpacity, View, StyleSheet } from 'react-native';
import { GoogleSignin } from '@react-native-google-signin/google-signin';
import { webClientId } from '../services/googleSignIn';

export default function LoginScreen({ navigation }: any) {
  useEffect(() => {
    GoogleSignin.configure({ webClientId });
  }, []);

  const registerOrLoginWithGoogle = async (userInfo: any) => {
    try {
      // 注意：如果用真机，请将localhost改为你电脑的局域网IP
      const response = await fetch('http://10.0.2.2:5050/api/users/google-login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          googleId: userInfo.id,
          email: userInfo.email,
          displayName: userInfo.name,
          username: userInfo.givenName,
          avatarUrl: userInfo.photo,
          authProvider: "GOOGLE",
        })
        
      });
      

      const user = await response.json();
      
      console.log('User from backend:', user);
      // 你可以在这里保存user到本地或全局状态
    } catch (err) {
      console.log('Failed to register/login with backend:', err);
    }
  };

  const handleLogin = async () => {
    try {
      await GoogleSignin.hasPlayServices();
      const userInfo = await GoogleSignin.signIn();
      console.log('Google userInfo:', userInfo);
      await registerOrLoginWithGoogle(userInfo.data?.user);
      navigation.replace('MainTabs');
    } catch (error: any) {
      Alert.alert('登录失败', error.message ? error.message : JSON.stringify(error));
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#ffffff" />
      <View style={styles.content}>
        <Text style={styles.title}>IA 真棒</Text>
        <Text style={styles.subtitle}>🤖 人工智能改变世界</Text>
        <TouchableOpacity style={styles.loginButton} onPress={handleLogin}>
          <Text style={styles.loginButtonText}>使用 Google 登录</Text>
        </TouchableOpacity>
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
    justifyContent: 'center',
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
  subtitle: {
    fontSize: 18,
    color: '#7f8c8d',
    textAlign: 'center',
    lineHeight: 24,
    marginBottom: 50,
  },
  loginButton: {
    backgroundColor: '#3498db',
    paddingHorizontal: 40,
    paddingVertical: 15,
    borderRadius: 25,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  loginButtonText: {
    color: '#ffffff',
    fontSize: 18,
    fontWeight: 'bold',
    textAlign: 'center',
  },
}); 