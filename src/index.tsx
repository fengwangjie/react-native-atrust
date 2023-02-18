import { NativeModules } from 'react-native';
const { RNSangforAtrustVpn } = NativeModules;

export function login(
  url: string,
  username: string,
  password: string
): Promise<any> {
  if (!RNSangforAtrustVpn) {
    Promise.reject('RNSangforAtrustVpn is not available');
  } else {
    return RNSangforAtrustVpn.login(url, username, password);
  }
  return Promise.resolve('sucesss');
}
