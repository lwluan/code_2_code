import request from '@/utils/request';
import Constants from '@/utils/constants';

export async function fakeAccountLogin(params) {
  return request(`${Constants.SERVICE_ROOT}/comm/admin/login`, {
    method: 'POST',
    data: params,
  });
}
export async function getFakeCaptcha(mobile) {
  return request(`/api/login/captcha?mobile=${mobile}`);
}
