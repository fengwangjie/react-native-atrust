import * as React from 'react';

import { Button, StyleSheet, View } from 'react-native';
import { init, login } from 'react-native-atrust';

export default function App() {
  const handleLogin = async () => {
    return init()
      .then(() => {
        return login('https://vpn.example.com', 'username', 'password').then(
          (res) => {
            console.log('login', res);
          }
        );
      })
      .finally(() => {
        console.log('finally');
      });
  };

  return (
    <View style={styles.container}>
      <Button title="登录" onPress={handleLogin} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
