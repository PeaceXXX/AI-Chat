import React from 'react';
import { Alert, SafeAreaView, StatusBar, Text, TouchableOpacity, View, StyleSheet } from 'react-native';
import { GoogleSignin } from '@react-native-google-signin/google-signin';

export default function MeScreen({ navigation }: any) {
  const handleSignOut = async () => {
    try {
      await GoogleSignin.signOut();
      navigation.replace('Login');
    } catch (error: any) {
      Alert.alert('登出失败', error.message ? error.message : JSON.stringify(error));
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#ffffff" />
      <View style={styles.content}>
        <Text style={styles.title}>Me</Text>
        <TouchableOpacity style={styles.loginButton} onPress={handleSignOut}>
          <Text style={styles.loginButtonText}>Sign Out</Text>
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