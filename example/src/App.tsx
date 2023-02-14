import * as React from 'react';

import { Button, StyleSheet, View } from 'react-native';
import { init, login } from 'react-native-atrust';

export default function App() {
  const atrustLogin = () => {
    init()
      .then(() => {
        login('http://127.0.0.1', 'username', 'password')
          .then((result) => {
            console.log('result', result);
          })
          .catch((err: any) => {
            console.log({ message: 'Login failed:' + err, type: 'danger' });
          });
      })
      .then(() => {
        console.log('init success');
      })
      .catch((err) => {
        console.log({ message: 'Atrust Init failed:' + err, type: 'danger' });
      });
  };

  return (
    <View style={styles.container}>
      <Button title="登录" onPress={atrustLogin} />
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
