import * as CryptoJS from 'crypto-js';
import MD5 from 'crypto-js/md5'

const AuthTokenKey = 'ZYZWUIEQIN11$^$*'; /** AES密钥 */
const AuthTokenIv = 'ZYZWUIEQIN11$^$*'; /** AES向量 */

/** AES加密 */
export function Encrypt(dataStr) {
  const encrypted = CryptoJS.AES.encrypt(dataStr, CryptoJS.enc.Utf8.parse(AuthTokenKey), {
    iv: CryptoJS.enc.Utf8.parse(AuthTokenIv),
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  });
  return encrypted.toString();
}

/** AES解密 */
export function Decrypt(data) {
  const data2 = data.replace(/\n/gm, '');
  const decrypted = CryptoJS.AES.decrypt(data2, CryptoJS.enc.Utf8.parse(AuthTokenKey), {
    iv: CryptoJS.enc.Utf8.parse(AuthTokenIv),
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  });
  return decrypted.toString(CryptoJS.enc.Utf8);
}

export function md5(str) {
  return MD5(str).toString();
}
