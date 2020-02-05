/** 存储内容 */
import { Encrypt, Decrypt } from './encryptUtil';

const userTokenKey = 'userTokenKey';
const userInfoKey = 'userInfoKey';
const appInfoKey = 'appInfoKey';

export function putAppInfo(appObjStr) {
  const key = Encrypt(appInfoKey);
  const value = Encrypt(appObjStr);
  localStorage.setItem(key, value);
}

export function getAppInfo() {
  // a,t,c
  const key = Encrypt(appInfoKey);
  let str = localStorage.getItem(key);
  if (str) {
    str = Decrypt(str);
    return JSON.parse(str);
  }
  return null;
}

export function putAccessToken(token) {
  const key = Encrypt(userTokenKey);
  const value = Encrypt(JSON.stringify(token));
  localStorage.setItem(key, value);
}

export function getAccessToken() {
  const key = Encrypt(userTokenKey);
  let str = localStorage.getItem(key);
  if (str) {
    str = Decrypt(str);
    return JSON.parse(str);
  }
  return null;
}

export function putUserInfo(userObj) {
  const key = Encrypt(userInfoKey);
  const value = Encrypt(JSON.stringify(userObj));
  localStorage.setItem(key, value);
}

export function getUserInfo() {
  const key = Encrypt(userInfoKey);
  let str = localStorage.getItem(key);
  if (str) {
    str = Decrypt(str);
    return JSON.parse(str);
  }
  return null;
}
