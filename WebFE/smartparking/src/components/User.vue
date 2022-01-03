<template>
  <div>
    <div class="search">
      <el-row :gutter="20">
        <el-col :span="3">
          <span>Họ tên :</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="userSearch.name"></el-input>
        </el-col>
        <el-col :span="3">
          <span type="text">Quyền: </span>
        </el-col>
        <el-col :span="4">
          <el-select v-model="userSearch.role" placeholder="Select" clearable>
            <el-option v-for="item in userTypeOption" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-col>
         <el-col :span="3">
          <span>Email :</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="userSearch.email"></el-input>
        </el-col>
        
      </el-row>
      <el-row :gutter="20">
        <el-col :span="3">
          <span>Tên tài khoản: </span>
        </el-col>
          <el-col :span="4">
          <el-input v-model="userSearch.username"></el-input>
        </el-col>
        <el-col :span="3">
          <span type="text">Số điện thoại :</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="userSearch.phoneNumber" type="text"></el-input>
        </el-col>
      </el-row>
      <el-row :gutter="4" type="flex" justify="start">
        <el-col :span="3">
          <el-button type="primary" plain round @click="searchData"><i class="el-icon-search"></i> Tìm kiếm
          </el-button>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" plain round @click="resetSearchInput">Thiết lập lại</el-button>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" plain round @click="dialogAddForm = true;"><i class="el-icon-plus"></i> Thêm mới
          </el-button>
        </el-col>
        <el-dialog title="Thêm tài khoản" :visible.sync="dialogAddForm">
          <el-form :model="userAdd">
            <el-form-item label="Họ tên " :label-width="formLabelWidth">
              <el-input v-model="userAdd.name"></el-input>
            </el-form-item>
            <el-form-item label="Số điện thoại" :label-width="formLabelWidth">
              <el-input v-model="userAdd.phoneNumber"></el-input>
            </el-form-item>
            <el-form-item label="Email" :label-width="formLabelWidth">
              <el-input v-model="userAdd.email"></el-input>
            </el-form-item>
            <el-form-item label="Quyền" :label-width="formLabelWidth">
              <el-select v-model="userAdd.role" clearable placeholder="Select">
                <el-option v-for="item in userTypeOption" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="Tên tài khoản" :label-width="formLabelWidth">
              <el-input v-model="userAdd.username" type="text"></el-input>
            </el-form-item>
            <el-form-item label="Mật khẩu" :label-width="formLabelWidth">
              <el-input v-model="userAdd.password" type="text"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogAddForm = false">Hủy</el-button>
            <el-button type="primary" @click="adduser">Xác nhận</el-button>
          </span>
        </el-dialog>
      </el-row>
    </div>
    <div class="table-info" style="width: 100%">
      <el-table :data="listusers" style="width: 100%">
        <el-table-column fixed prop="id" label="ID" width="40">
        </el-table-column>
        <el-table-column prop="name" label="Họ tên" width="140">
        </el-table-column>
        <el-table-column prop="role" label="Quyền" width="165">
        </el-table-column>
        <el-table-column prop="phoneNumber" label="Số điện thoại" width="120">
        </el-table-column>
        <el-table-column prop="email" label="Email" width="100">
        </el-table-column>
        <el-table-column prop="username" label="Tên tài khoản" width="180">
        </el-table-column>
        <el-table-column width="150">
          <template slot-scope="scope">
            <el-button @click="openDialogClone(scope.$index, scope.row)" type="text" size="small">Sao chép</el-button>
            <el-button @click="openDialogEdit(scope.$index, scope.row)" type="text" size="small">Sửa</el-button>
            <el-button type="text" size="small" @click="openDeleteDialog(scope.$index, scope.row)">Xóa</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :title="dialogEditTitle " :visible.sync="dialogEditForm">
        <el-form :model="userEdit">
          
          <el-form-item label="Họ tên" :label-width="formLabelWidth">
            <el-input v-model="userEdit.name"></el-input>
          </el-form-item>
          <el-form-item label="Số điện thoại" :label-width="formLabelWidth">
            <el-input v-model="userEdit.address"></el-input>
          </el-form-item>
          <el-form-item label="Email" :label-width="formLabelWidth">
            <el-input v-model="userEdit.email"></el-input>
          </el-form-item>
          <el-form-item label="Quyền" :label-width="formLabelWidth">
            <el-select v-model="userEdit.role" clearable placeholder="Select">
              <el-option v-for="item in userTypeOption" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Tên tài khoản" :label-width="formLabelWidth">
            <el-input v-model="userEdit.username"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogEditForm = false">Huỷ</el-button>
          <el-button type="primary" @click="edituser">Xác nhận</el-button>
        </span>
      </el-dialog>
      <el-dialog title="Cảnh báo" :visible.sync="dialogDeleteVisible" width="30%">
        <span>Xác nhận xóa người dùng {{userDelete.name}}?</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">Huỷ</el-button>
          <el-button type="primary" @click="deleteuser">Xác nhận</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>
<style scoped>
.search {
    height: 10%vh;
  }

  .table-info {
    height: 90%vh;
  }

  .el-row {
    margin-bottom: 20px;
  }

  .el-col>span {
    line-height: 40px;
    font-size: 16px;
  }
</style>
<script>
import {
    mapActions
  } from "Vuex";
  import moment from "moment";
  export default {
    name: "User",
    data() {
      return {
        passwordRetype:'',
        userSearch: {
          phoneNumber: '',
          id: null,
          name: '',
          email: null,
          username: null,
          role: null
        },
        listusers: [],
        dialogEditForm: false,
        dialogAddForm: false,
        formLabelWidth: "200px",
        dialogDeleteVisible: false,
        dialogEditTitle: '',
        editType: "",
        userTypeOption: [{
            value: 1,
            label: "Quản trị",
          },
          {
            value: 2,
            label: "Người dùng",
          },
          {
            value: 3,
            label: "Khách",
          },
        ],
        userEdit: {
          phoneNumber: '',
          id: null,
          name: '',
          email: null,
          username: null,
          role: null
        },
        userAdd: {
          phoneNumber: '',
          id: null,
          name: '',
          email: null,
          username: null,
          role: null,
          password: null,
        },
        userDelete: {
          phoneNumber: '',
          id: null,
          name: '',
          email: null,
          username: null,
          role: null
        }
      };
    },
    methods: {
      ...mapActions(["getAllUserService", "updateUserService", "addUserService", "deleteUserService",
        "searchUserService"
      ]),
      openDialogEdit(index, row) {
        this.dialogEditForm = true;
        this.dialogEditTitle = "Sửa thông tin";
        this.editType = "edit";
        this.userEdit.id = row.id;
        this.userEdit.name = row.name;
        this.userEdit.username = row.username;
        this.userEdit.phoneNumber = row.phoneNumber;
        this.userEdit.role = this.getUserRoleCode(row.role);
        this.userEdit.email = row.email;
      },
      openDialogClone(index, row) {
        this.dialogEditForm = true;
        this.dialogEditTitle = "Sao chép thông tin";
        this.editType = "copy";
        this.userEdit.id = row.id;
        this.userEdit.name = row.name;
        this.userEdit.username = row.username;
        this.userEdit.phoneNumber = row.phoneNumber;
        this.userEdit.role = row.role;
        this.userEdit.email = row.email;
      },
      edituser() {
        this.userEdit.role = this.getUserRoleCode(this.userEdit.role);
        if (this.editType === "edit") {
          let fd= new FormData();
          fd.append('user', JSON.stringify(this.userEdit))
          this.updateUserService(fd).then(data => {
            this.dialogEditForm = false
            this.reload();
          });
        }else if(this.editType==="copy"){
          this.userEdit.id=null;
          this.addUserService(this.userEdit).then(data=>{
            this.dialogEditForm = false
            this.reload();
          })
        }

      },
      openDeleteDialog(index, row) {
        this.dialogDeleteVisible = true
        this.userDelete = row
      },
      deleteuser() {
        debugger
        this.deleteUserService(this.userDelete.id).then(data => {
          this.dialogDeleteVisible = false;
          this.reload();
        })
      },
      adduser() {
        let fd = new FormData();
        fd.append('user', JSON.stringify(this.userAdd))
        this.addUserService(fd).then(data => {
          this.dialogAddForm = false;
          this.reload();
        })
      },
      searchData() {
        console.log(this.userSearch);
        this.searchUserService(JSON.stringify(this.userSearch)).then(data => {
          this.listusers = [];
          this.handleData(data);
        })
      },
      dateFormat(value) {
        if (value) {
          return moment(String(value)).format("DD/MM/yyyy");
        } else {
          return "";
        }
      },
      getUserRoleCode(role){
        if(role==="Quản trị"){
          return "ROLE_ADMIN";
        }else if (role==="Người dùng"){
          return "ROLE_USER";
        }else if(role==="Khách"){
          return "ROLE_GUEST";
        }else {
          return role;
        }
      },
      getUserRoleLabel(role){
        if(role==="ROLE_ADMIN"){
          return "Quản trị";
        }else if (role==="ROLE_USER"){
          return "Người dùng";
        }else if(role==="ROLE_GUEST"){
          return "Khách";
        }else {
          return role;
        }
      },
      
      reload() {
        this.listusers = [];
        this.getAllUserService().then((data) => {
          this.handleData(data);
        });
      },
      handleData(data) {
        data.forEach((element) => {
          // element.createdDate = this.dateFormat(element.createdDate);
          element.role = this.getUserRoleLabel(element.role);
          // element.status = this.getuserStatusLabel(element.status);
          this.listusers.push(element);
        });
      },
      resetSearchInput() {
        this.userSearch.address = "";
        this.userSearch.id = "";
        this.userSearch.status = null;
        this.userSearch.positionId = "";
        this.userSearch.type = null
        this.userSearch.name = "";
      }
    },
    created() {
      this.reload();
    },
  };

</script>
