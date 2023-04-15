import { NativeModules } from 'react-native';
const { RNSangforAtrustVpn } = NativeModules;

export function login(
  url: string,
  username: string,
  password: string
): Promise<any> {
  if (!RNSangforAtrustVpn) {
    return Promise.reject('sdk组件未安装');
  } else {
    return RNSangforAtrustVpn.login(url, username, password);
  }
}

export function getStatus(): Promise<any> {
  if (!RNSangforAtrustVpn) {
    return Promise.reject('sdk组件未安装');
  } else {
    return RNSangforAtrustVpn.getVpnStatus();
  }
}
