import { notification } from 'antd';
import { queryPageListApi, addEntityApi, detailEntityApi, updateEntityApi, removeEntityApi } from './service';
import { queryPageListApi as queryPageRoleList } from '../sys-role/service';

export default {
  namespace: 'sysUser',
  state: {
    entityPageResult: {
      list: [],
      pagination: { total: 12, pageSize: 1, current: 1 },
    },
    entityInfoObj: {},
    roleList: [], // 添加页面角色列表
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

      // add or update
      let response;
      if (payload.id) {
        response = yield call(updateEntityApi, payload);
      } else {
        response = yield call(addEntityApi, payload);
      }

      if (response.code === 10000) {
        notification.error({
          message: '数据提交成功!',
        });
        if (callback) callback(response);
      }
    },

    * removeEntity({ payload, callback }, { call }) {
      const response = yield call(removeEntityApi, payload);
      if (response.code === 10000) {
        notification.error({
          message: '删除成功！',
        });
        if (callback) callback(response);
      }
    },

    * queryAllRoleList(_, { call, put }) {
      const response = yield call(queryPageRoleList, {
        currPage: 1,
        pageSize: 100,
        status: 'enable',
      });
      yield put({
        type: 'changeRoleList',
        payload: { roleList: response.data.rows },
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
    changeRoleList(state, { payload }) {
      return {
        ...state,
        roleList: payload.roleList,
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
