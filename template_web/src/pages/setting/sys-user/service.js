import { stringify } from 'qs';
import request from '@/utils/request';
import Constants from '@/utils/constants';

const serviceName = 'sysUser';
export async function queryPageListApi(params) {
  return request(`${Constants.SERVICE_ROOT}/adapi/${serviceName}/entityPage?${stringify(params)}`);
}

export async function detailEntityApi(id) {
  return request(`${Constants.SERVICE_ROOT}/adapi/${serviceName}/entityInfo/${id}`);
}

export async function updateEntityApi(params) {
  return request(`${Constants.SERVICE_ROOT}/adapi/${serviceName}/entityInfo`, {
    method: 'PUT',
    data: {
      ...params,
    },
  });
}

export async function addEntityApi(params) {
  return request(`${Constants.SERVICE_ROOT}/adapi/${serviceName}/entityInfo`, {
    method: 'POST',
    data: {
      ...params,
    },
  });
}

export async function removeEntityApi(params) {
  return request(`${Constants.SERVICE_ROOT}/adapi/${serviceName}/entityInfo`, {
    method: 'DELETE',
    data: {
      ...params,
    },
  });
}
