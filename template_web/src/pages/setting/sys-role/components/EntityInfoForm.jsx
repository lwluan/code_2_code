import React, { PureComponent } from 'react';
import {
  Form,
  Input,
  Modal,
  Tree,
} from 'antd';

const { TreeNode } = Tree;
const FormItem = Form.Item;
const { useState } = React;

@Form.create()
class EntityInfoForm extends PureComponent {
  state = {
    expandedKeys: [],
    autoExpandParent: true,
    checkedKeys: [],
    selectedKeys: [],
  };

  locadRoleData = entityInfoObj => {
    const { state } = this;
    this.setState({ ...state, checkedKeys: entityInfoObj.authIds });
  };

  componentDidMount = () => {
    const { onRef } = this.props;
    onRef(this);
  };

  okHandle = () => {
    const { form, handleEntityUpdate } = this.props;
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      console.log(fieldsValue);
      handleEntityUpdate({
        ...fieldsValue,
        authIds: this.state.checkedKeys,
      });
    });
  };

  onExpand = expandedKeys => {
    const { state } = this
    this.setState({
      ...state,
      expandedKeys,
      autoExpandParent: false,
    });
  };

  onCheck = checkedKeys => {
    const { state } = this
    this.setState({
      ...state,
      checkedKeys,
    });
  };

  onSelect = (selectedKeys, info) => {
    const { state } = this
    this.setState({
      ...state,
      selectedKeys,
    });
  };

  renderTreeNodes = data => {
    const { authoritys } = this.props;
    return data.map(item => {
      const children = authoritys.filter(aa => aa.pid === item.guid);
      if (children.length > 0) {
        return (
          <TreeNode title={item.name} key={item.guid} dataRef={item}>
            {this.renderTreeNodes(children)}
          </TreeNode>
        );
      }
      return <TreeNode {...item} key={item.guid} title={item.name}/>;
    });
  };

  render() {
    const {
      modalVisible, form, handleEntityFormModalVisible,
      authoritys, entityInfoObj,
    } = this.props;

    const treeRoot = authoritys.filter(item => !item.pid);
    const { expandedKeys, autoExpandParent, checkedKeys, selectedKeys } = this.state;
    return (
      <Modal
        destroyOnClose
        title="角色详情"
        visible={modalVisible}
        onOk={this.okHandle}
        onCancel={() => handleEntityFormModalVisible()}
      >
        <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="角色名称">
          {form.getFieldDecorator('name', {
            initialValue: entityInfoObj.name,
            rules: [{
              required: true,
              message: '请输入至少角色名称！',
              min: 2,
            }],
          })(<Input placeholder="请输入"/>)}
        </FormItem>

        <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="访问权限">
          <Tree
            checkable
            onExpand={this.onExpand}
            expandedKeys={expandedKeys}
            autoExpandParent={autoExpandParent}
            onCheck={this.onCheck}
            checkedKeys={checkedKeys}
            onSelect={this.onSelect}
            selectedKeys={selectedKeys}
          >
            {this.renderTreeNodes(treeRoot)}
          </Tree>
        </FormItem>


      </Modal>
    );
  }
}

export default EntityInfoForm;
