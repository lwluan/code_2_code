import { notification } from 'antd';
import { queryPageListApi, addEntityApi, detailEntityApi, updateEntityApi, removeEntityApi, queryAllAuthoritysApi } from './service';

export default {
  namespace: 'sysRole',
  state: {
    entityPageResult: {
      list: [],
      pagination: { total: 12, pageSize: 1, current: 1 },
    },
    entityInfoObj: {},
    authoritys: [], // 添加页面角色列表
  },

  effects: {
    * queryEntityPageList({ payload }, { call, put }) {
      const response = yield call(queryPageListApi, payload);

      const entityPageResult = {
        list: response.data.rows,
        pagination: {
          total: response.data.totalCount,
          pageSize: response.data.pageSize,
          current: response.data.currPage,
        },
      };
      yield put({
        type: 'changeEntityPageList',
        payload: { code: response.code, entityPageResult },
      });
    },

    * detailEntity({ payload }, { call, put }) {
      const response = yield call(detailEntityApi, payload);

      if (response.code === 10000) {
        yield put({
          type: 'changeDetailEntity',
          payload: response.data,
        });
      }
    },

    * updateEntity({ payload, callback }, { call }) {
      let response;
      if (payload.id) {
        response = yield call(updateEntityApi, payload);
      } else {
        response = yield call(addEntityApi, payload);
      }

      if (response.code === 10000) {
        notification.success({
          message: '数据提交成功!',
        });
        if (callback) callback(response);
      }
    },

    * removeEntity({ payload, callback }, { call }) {
      const response = yield call(removeEntityApi, payload);
      if (response.code === 10000) {
        notification.success({
          message: '删除成功！',
        });
        if (callback) callback(response);
      }
    },

    * queryAllAuthoritys(_, { call, put }) {
      const response = yield call(queryAllAuthoritysApi);
      yield put({
        type: 'changeAuthoritysList',
        payload: { authoritys: response.data },
      });
    },
  },

  reducers: {
    changeEntityPageList(state, { payload }) {
      return {
        ...state,
        entityPageResult: payload.entityPageResult,
      };
    },
    changeAuthoritysList(state, { payload }) {
      return {
        ...state,
        authoritys: payload.authoritys,
      };
    },
    changeDetailEntity(state, { payload }) {
      return {
        ...state,
        entityInfoObj: payload,
      };
    },
  },
};
