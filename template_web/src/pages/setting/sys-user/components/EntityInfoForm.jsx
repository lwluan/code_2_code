import React from 'react';
import {
  Form,
  Input,
  Select,
  Modal,
  Radio,
} from 'antd';

const FormItem = Form.Item;
const { Option } = Select;


const EntityInfoForm = props => {

  const { modalVisible, form, handleEntityUpdate, handleEntityFormModalVisible, roleList, entityInfoObj } = props;

  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      handleEntityUpdate(fieldsValue);
    });
  };

  let passwordRule = { rules: [{ required: true, message: '请输入密码！' }] };
  if (entityInfoObj.id) {
    passwordRule = {};
  }

  return (
    <Modal
      destroyOnClose
      title="用户详情"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleEntityFormModalVisible()}
    >
      <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="账号">
        {form.getFieldDecorator('username', {
          initialValue:entityInfoObj.username,
          rules: [{ required: true, message: '请输入账号！' }],
        })(<Input placeholder="请输入账号" />)}
      </FormItem>
      <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="密码">
        {form.getFieldDecorator('password', {
          ...passwordRule,
        })(<Input placeholder="请输入" />)}
      </FormItem>
      <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="姓名">
        {form.getFieldDecorator('nickname', {
          initialValue:entityInfoObj.nickname,
          rules: [{ required: true, message: '请输入姓名！' }],
        })(<Input placeholder="请输入姓名" />)}
      </FormItem>
      <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="手机">
        {form.getFieldDecorator('mobile', {
          initialValue:entityInfoObj.mobile,
          rules: [{ required: true, message: '请输入手机！' }],
        })(<Input placeholder="请输入手机" />)}
      </FormItem>
      <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="邮箱">
        {form.getFieldDecorator('email', {
          initialValue:entityInfoObj.email,
          rules: [{ required: true, message: '请输入邮箱！' }],
        })(<Input placeholder="请输入邮箱" />)}
      </FormItem>

      <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="状态">
        {form.getFieldDecorator('status', {
          initialValue:entityInfoObj.status,
          rules: [{ required: true, message: '请输入状态！' }],
        })(
          <Radio.Group>
            <Radio value="disable">禁用</Radio>
            <Radio value="enable">启用</Radio>
          </Radio.Group>
        )}
      </FormItem>

      <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="角色">
        {form.getFieldDecorator('roles', {
          initialValue:entityInfoObj.roles,
          rules: [{ required: true, message: '请选择角色！' }],
        })(
          <Select
            mode="multiple"
            placeholder="请选择角色"
            style={{
              margin: '8px 0',
              minWidth: '120px',
            }}
          >
            {roleList.map(v => {
              return (
                <Option key={v.id} value={v.id}>
                  {v.name}
                </Option>
              );
            })}
          </Select>
        )}
      </FormItem>


    </Modal>
  );
}

export default Form.create()(EntityInfoForm);
