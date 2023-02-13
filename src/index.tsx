import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-atrust' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const AtrustVpn = NativeModules.RNSangforAtrustVpn
  ? NativeModules.RNSangforAtrustVpn
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function init(): Promise<any> {
  return AtrustVpn.init();
}

export function login(
  url: string,
  username: string,
  password: string
): Promise<any> {
  init();
  return AtrustVpn.login(url, username, password);
}
