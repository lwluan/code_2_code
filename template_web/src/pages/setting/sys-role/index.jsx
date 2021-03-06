import {
  Badge,
  Button,
  Card,
  Col,
  DatePicker,
  Divider,
  Dropdown,
  Form,
  Icon,
  Input,
  InputNumber,
  Menu,
  Row,
  Select,
  Modal,
} from 'antd';
import React, { Component, Fragment } from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { connect } from 'dva';
import moment from 'moment';
import EntityInfoForm from './components/EntityInfoForm';
import StandardTable from '../../list/table-list/components/StandardTable';
import styles from '../../list/table-list/style.less';

const FormItem = Form.Item;
const { Option } = Select;

const entityNameSpace = 'sysRole';

@connect(({ sysRole, loading }) => ({
  sysRole,
  loading: loading.models.sysRole,
}))
class SysRole extends Component {
  state = {
    modalVisible: false,
    expandForm: false,
    selectedRows: [],
    formValues: {},

    current: 1,
    pageSize: 12,
    sorter: '',
  };

  columns = [
    {
      title: '角色名称',
      dataIndex: 'name',
      render: text => <span>{text}</span>,
    },
    {
      title: '创建时间',
      dataIndex: 'create_date',
      sorter: true,
      render: val => <span>{moment(val).format('YYYY-MM-DD HH:mm:ss')}</span>,
    },
    {
      title: '更新时间',
      dataIndex: 'update_date',
      sorter: true,
      render: val => <span>{moment(val).format('YYYY-MM-DD HH:mm:ss')}</span>,
    },
    {
      title: '操作',
      render: (text, record) => (
        <Fragment>
          <a onClick={this.handleEntityFormModalVisible.bind(this, true, record.id)}>修改</a>
          <Divider type="vertical" />
          <a onClick={this.handleRemoveEntity.bind(this, record)}>删除</a>
        </Fragment>
      ),
    },
  ];

  EntityInfoForm;

  componentDidMount() {
    this.handleReloadTableList();
  }

  componentDidUpdate = () => {
    if (this.EntityInfoForm) {
      const {
        [entityNameSpace]: { entityInfoObj },
      } = this.props;
      this.EntityInfoForm.locadRoleData(entityInfoObj);
    }
  };

  handleReloadTableList = () => {
    const { dispatch } = this.props;
    const { formValues, current, pageSize, sorter } = this.state;

    const params = {
      current,
      pageSize,
      ...formValues,
      sorter,
    };

    dispatch({
      type: `${entityNameSpace}/queryEntityPageList`,
      payload: params,
    });
  }

  handleStandardTableChange = (pagination, filtersArg, sorter) => {
    if (sorter.field) {
      this.state.sorter = `${sorter.field}_${sorter.order}`;
    } else {
      this.state.sorter = '';
    }
    this.state.current = pagination.current;
    this.state.pageSize = pagination.pageSize;

    this.handleReloadTableList();
  };

  handleFormReset = () => {
    const { form, dispatch } = this.props;
    form.resetFields();
    this.setState({
      formValues: {},
    });
    dispatch({
      type: `${entityNameSpace}/queryEntityPageList`,
      payload: {},
    });
  };

  toggleForm = () => {
    const { expandForm } = this.state;
    this.setState({
      expandForm: !expandForm,
    });
  };

  handleMenuClick = e => {
    const { dispatch } = this.props;
    const { selectedRows } = this.state;
    if (!selectedRows) return;

    switch (e.key) {
      case 'remove':

        break;

      default:
        break;
    }
  };

  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    });
  };

  handleSearch = e => {
    e.preventDefault();
    const { form } = this.props;
    form.validateFields((err, fieldsValue) => {
      if (err) return;

      const values = {
        ...fieldsValue,
        createTime: fieldsValue.createTime && fieldsValue.createTime.valueOf(),
      };

      this.state.formValues = values;
      this.state.current = 1;
      this.handleReloadTableList();
    });
  };

  handleEntityFormModalVisible = (flag, entityId) => {
    this.setState({
      modalVisible: !!flag,
    });

    if (flag === true) {
      const { dispatch } = this.props;
      dispatch({
        type: `${entityNameSpace}/queryAllAuthoritys`,
      });

      if (entityId) {
        dispatch({
          type: `${entityNameSpace}/detailEntity`,
          payload: entityId,
        });
      } else {
        dispatch({
          type: `${entityNameSpace}/changeDetailEntity`,
          payload: {},
        });
      }
    }
  };


  handleEntityUpdate = fieldsValue => {
    const { dispatch, [entityNameSpace]: { entityInfoObj } } = this.props;

    let formData = fieldsValue;
    if (entityInfoObj.id) {
      formData = { ...fieldsValue, id: entityInfoObj.id };
    }

    dispatch({
      type: `${entityNameSpace}/updateEntity`,
      payload: formData,
      callback: () => {
        this.handleEntityFormModalVisible();
        this.handleReloadTableList();
      },
    });
  };

  handleRemoveEntity = record => {
    const { dispatch } = this.props;

    Modal.confirm({
      title: '确认',
      content: '是否确认删除',
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        dispatch({
          type: `${entityNameSpace}/removeEntity`,
          payload: record.id,
          callback: () => {
            this.handleReloadTableList();
          },
        });
      },
    });
  };

  renderSimpleForm() {
    const {
      form: { getFieldDecorator },
    } = this.props;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={8} sm={24}>
            <FormItem label="账号名称">
              {getFieldDecorator('username')(<Input placeholder="昵称/账号/手机号/邮箱" />)}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="使用状态">
              {getFieldDecorator('status')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="">全部</Option>
                  <Option value="enable">启用</Option>
                  <Option value="disable">禁用</Option>
                </Select>)}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">
                查询
              </Button>
              <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>
                重置
              </Button>
              <a style={{ marginLeft: 8 }} onClick={this.toggleForm}>
                展开 <Icon type="down" />
              </a>
            </span>
          </Col>
        </Row>
      </Form>
    );
  }

  renderAdvancedForm() {
    const {
      form: { getFieldDecorator },
    } = this.props;

    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={8} sm={24}>
            <FormItem label="账号名称">
              {getFieldDecorator('username')(<Input placeholder="昵称/账号/手机号/邮箱" />)}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="使用状态">
              {getFieldDecorator('status')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="">全部</Option>
                  <Option value="enable">启用</Option>
                  <Option value="disable">禁用</Option>
                </Select>)}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">
                查询
              </Button>
              <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>
                重置
              </Button>
              <a style={{ marginLeft: 8 }} onClick={this.toggleForm}>
                展开 <Icon type="down" />
              </a>
            </span>
          </Col>
        </Row>
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={8} sm={24}>
            <FormItem label="创建日期">
              {getFieldDecorator('createTime')(
                <DatePicker style={{ width: '100%' }} placeholder="请输入更新日期" />)}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>

          </Col>
          <Col md={8} sm={24}>
            {' '}
          </Col>
        </Row>
      </Form>
    );
  }

  renderForm() {
    const { expandForm } = this.state;
    return expandForm ? this.renderAdvancedForm() : this.renderSimpleForm();
  }

  render() {
    const {
      [entityNameSpace]: { entityPageResult, entityInfoObj, authoritys },
      loading,
    } = this.props;
    const { selectedRows, modalVisible } = this.state;
    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="remove">删除</Menu.Item>
        <Menu.Item key="approval">批量审批</Menu.Item>
      </Menu>
    );
    const parentMethods = {
      handleEntityUpdate: this.handleEntityUpdate,
      handleEntityFormModalVisible: this.handleEntityFormModalVisible,
      entityInfoObj,
      authoritys,
    };
    return (
      <PageHeaderWrapper>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListForm}>{this.renderForm()}</div>
            <div className={styles.tableListOperator}>
              <Button icon="plus" type="primary" onClick={() => this.handleEntityFormModalVisible(true)}>
                新建
              </Button>
              {selectedRows.length > 0 && (
                <span>
                  <Button>批量操作</Button>
                  <Dropdown overlay={menu}>
                    <Button>
                      更多操作 <Icon type="down" />
                    </Button>
                  </Dropdown>
                </span>
              )}
            </div>
            <StandardTable
              rowKey="id"
              selectedRows={selectedRows}
              loading={loading}
              data={entityPageResult}
              columns={this.columns}
              onSelectRow={this.handleSelectRows}
              onChange={this.handleStandardTableChange}
            />
          </div>
        </Card>
        <EntityInfoForm onRef={ele => {
          this.EntityInfoForm = ele;
        }} {...parentMethods} modalVisible={modalVisible} />
      </PageHeaderWrapper>
    );
  }
}

export default Form.create()(SysRole);
