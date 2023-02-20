import * as React from 'react';
import { useState } from 'react';

import {
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { login } from 'react-native-atrust';

export default function App() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [url, setUrl] = useState('');
  const atrustLogin = () => {
    login(url, username, password)
      .then((result) => {
        console.log('result', result);
      })
      .catch((err: any) => {
        console.log({ message: 'Login failed:' + err, type: 'danger' });
      });
  };

  return (
    <View style={styles.container}>
      <Text>Sangfor Aturst test</Text>
      <View style={styles.inputView}>
        <TextInput
          style={styles.TextInput}
          placeholder="请输入VpnUrl"
          placeholderTextColor="#003f5c"
          onChangeText={(url) => setUrl(url)}
        />
      </View>
      <View style={styles.inputView}>
        <TextInput
          style={styles.TextInput}
          placeholder="请输入用户名"
          placeholderTextColor="#003f5c"
          onChangeText={(username) => setUsername(username)}
        />
      </View>
      <View style={styles.inputView}>
        <TextInput
          style={styles.TextInput}
          placeholder="请输入密码"
          placeholderTextColor="#003f5c"
          secureTextEntry
          // eslint-disable-next-line @typescript-eslint/no-shadow
          onChangeText={(password: string) => setPassword(password)}
        />
      </View>
      <TouchableOpacity style={styles.loginBtn} onPress={atrustLogin}>
        <Text>登录</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  inputView: {
    width: '80%',
    backgroundColor: '#bae7ff',
    borderRadius: 25,
    height: 45,
    marginBottom: 10,
    alignItems: 'center',
  },
  TextInput: {
    height: 50,
    flex: 1,
    padding: 5,
    marginLeft: 20,
  },
  loginBtn: {
    width: '80%',
    borderRadius: 25,
    height: 50,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 20,
    backgroundColor: '#40a9ff',
  },
});
