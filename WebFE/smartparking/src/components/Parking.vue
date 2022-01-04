<template>
  <div style="font-size: 16px">
    <el-row class="searchParking" :gutter="10">
      <el-col :span="3" class="center"><span class="search-label">Tên bãi đỗ: </span>
      </el-col>
      <el-col :span="4">
        <el-input placeholder="Nhập tên bãi đỗ" v-model="parkingName"></el-input>
      </el-col>
      <el-col :span="3" class="center"><span class="search-label">Mã bãi đỗ: </span>
      </el-col>
      <el-col :span="4">
        <el-input placeholder="Nhập mã bãi đỗ" v-model="parkingCode"></el-input>
      </el-col>
      <!-- <el-col :span="3"><span class="search-label">Loại bãi đỗ: </span>
      </el-col>
      <el-col :span="4">
        <el-select v-model="parkingType" filterable placeholder="Chọn loại bãi đỗ" autocomplete>
          <el-option label="Xe máy" value="1" ></el-option>
          <el-option label="Ô tô" value="2"></el-option>
        </el-select>
      </el-col> -->
    </el-row>

    <el-row class="buttonSearch" justify="center" style="margin-top: 16px">
      <el-button type="primary" plain round>
        <i class="el-icon-search"></i> Tìm kiếm</el-button>
    </el-row>


    <el-dialog :visible.sync="parkingAreaDetail" width="80%">
      <el-carousel height="400px" :autoplay="false">
        <el-carousel-item v-for="(item, index1) in viewNumber" :key="index1">
          <el-row style="padding-top:16px">
            <el-col :span="2" v-for="(parkingCol, index2) in item" :key="index2"
              v-bind:class="{ active: true, 'col-margin': (index2) % 2, 'first-margin': index2%8==0 }">

              <el-card :body-style="{ padding: '4px' }" v-for="(parkingslot, index3) in parkingCol" :key="index3"
                style="text-align: center">
                <el-tooltip class="item" effect="dark" :content="''+parkingslot.id" placement="top">
                  <img src="../assets/car.png" alt="" v-if="parkingslot.status==1" />
                  <el-button v-if="parkingslot.status==0" style="border:none" type="text">{{parkingslot.id}}</el-button>
                </el-tooltip>
              </el-card>
            </el-col>
          </el-row>

        </el-carousel-item>
      </el-carousel>
      <span slot="footer" class="dialog-footer">
        <el-button @click="reloadParking">Tải lại</el-button>
        <el-button @click="parkingAreaDetail = false">Đóng</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="parkingEditDialog" width="80%" title="Sửa bãi đỗ">
      <el-row type="flex" justify="space-between">
        <el-col :span="6" class="parking-area-info">
          <el-form :model="parkingAreaEdit">
            <el-form-item label="Mã bãi đỗ" :label-width="formLabelWidth">
              <el-input v-model="parkingAreaEdit.id"></el-input>
            </el-form-item>
            <el-form-item label="Tên bãi đỗ" :label-width="formLabelWidth">
              <el-input v-model="parkingAreaEdit.name"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="8" class="parking-slot-info">
          <h5>Danh sách vị trí đỗ</h5>
          <el-table :data="listParkingSlotPage">
            <el-table-column fixed prop="id" label="Mã vị trí" width="150">
            </el-table-column>
            <el-table-column prop="status" label="Trạng thái" width="160">
            </el-table-column>
            <!-- <el-table-column prop="type" label="Loại bãi đỗ" width="300">
      </el-table-column> -->
            <el-table-column label="Thao tác" width="120">
              <template slot-scope="scope">
                <el-button @click="showParkingDetail(scope.$index, scope.row)" type="text" size="small">Xóa
                </el-button>
                <el-button type="text" size="small" @click="showParkingEdit(scope.$index, scope.row)">Sửa</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="block">
            <!-- <span class="demonstration">When you have few pages</span> -->
            <el-pagination layout="prev, pager, next" :total="listParkingSlot.length" :page-size="10" @current-change="handleCurrentChangeSlot">
            </el-pagination>
          </div>
        </el-col>
        <el-col :span='4'>
          <el-button>Thêm</el-button>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="saveParkingArea">Lưu</el-button>
        <el-button @click="parkingEditDialog = false">Đóng</el-button>
      </span>
    </el-dialog>

    <el-table :data="listParkingAreaPage" style="width: 100%; height: 620px" max-height="620" v-loading="loadingArea">
      <el-table-column fixed prop="id" label="Mã bãi đỗ" width="150">
      </el-table-column>
      <el-table-column prop="name" label="Tên bãi đỗ" width="160">
      </el-table-column>
      <el-table-column prop="numNotInUsed" label="Chỗ chưa đỗ" width="120">
      </el-table-column>
      <el-table-column prop="maxNumber" label="Số lượng tối đa" width="140">
      </el-table-column>
      <el-table-column prop="createdDate" label="Ngày tạo" width="160">
      </el-table-column>
      <!-- <el-table-column prop="type" label="Loại bãi đỗ" width="300">
      </el-table-column> -->
      <el-table-column label="Thao tác" width="120">
        <template slot-scope="scope">
          <el-button @click="showParkingDetail(scope.$index, scope.row)" type="text" size="small">Chi tiết</el-button>
          <el-button type="text" size="small" @click="showParkingEdit(scope.$index, scope.row)">Sửa</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-row class="pagination">
      <el-pagination layout="prev, pager, next" :total="listParkingArea.length" :page-size="10" @current-change="handleCurrentChangeArea">
      </el-pagination>
    </el-row>
  </div>
</template>
<style scoped>
  .search-label {
    line-height: 40px;
  }

  .el-card {
    height: 40px;
    margin: 4px;
    border: 1px solid rgb(15, 114, 226);
    border-radius: 10px;
  }

  img {
    object-fit: cover;
    width: 75px;
    height: 30px;
  }

  .col-margin {
    margin-right: 64px;
  }

  .first-margin {
    margin-left: 128px;
  }

  .el-carousel__item:nth-child(n) {
    background-color: #9bacc4;
    border-radius: 5px;
  }

</style>
<script>
  import {
    mapActions
  } from "Vuex";
  import moment from 'moment';
  export default {
    name: "Parking",
    data() {
      return {
        parkingType: null,
        parkingName: "",
        parkingCode: "",
        parkingAreaDetail: false,
        parkingEditDialog: false,
        parkingAreaViewIndex: null,
        formLabelWidth: '120px',
        listParkingSlot: [],
        listParkingSlotPage:[],
        parkingAreaEdit: {
          id: null,
          name: null,
          parkingSlots: []
        },
        parkingArea: [
          [{
              status: 1,
              id: "A12",
            },
            {
              status: 1,
              id: "A12",
            },
            {
              status: 1,
              id: "A12",
            },
          ],
        ],
        listParkingArea: [],
        listParkingAreaPage: [],
        viewNumber: [],
        listParkingAreaLength: 0,
        loadingArea: false

      };
    },
    methods: {
      ...mapActions(['getParkingAreaService', 'getParkingSlotsByIdService', 'getEmptySlotByParkingAreaId',
        'getAllParkingSlot'
      ]),
      handleCurrentChangeSlot(val){
        // console.log(this.listParkingSlot);
        var len=this.listParkingSlot.length;
        var end;
        if(len<val*10)
          end=len;
        else
          end=val*10;
        
      
        this.listParkingSlotPage=this.listParkingSlot.slice((val-1)*10, end);
      },
      handleCurrentChangeArea(val){
        var len=this.listParkingArea.length;
        var end;
        if(len<val*10)
          end=len;
        else
          end=val*10;
        
      
        this.listParkingAreaPage=this.listParkingArea.slice((val-1)*10, end);
      },
      handleDetail() {},
      showParkingEdit(index, row) {
        this.parkingEditDialog = true;
        this.parkingAreaEdit.id = row.id;
        this.parkingAreaEdit.name = row.name;
        this.listParkingArea.forEach(element=>{
          if(element.id==row.id){
            this.listParkingSlot=element.parkingSlots;
          }
        })
        // this.listParkingSlot = this.listParkingArea[index].parkingSlots;
        if(this.listParkingSlot.length>10)
          this.listParkingSlotPage=this.listParkingSlot.slice(0, 10);
        else{
          this.listParkingSlotPage=this.listParkingSlot.slice(0, this.listParkingSlot.length);
        }
      },
      saveParkingArea() {

      },
      showParkingDetail(index, row) {
        this.viewNumber = [];
        this.parkingAreaDetail = true;
        this.parkingAreaViewIndex = index
        this.getViewDataParkingArea(index);
      },
      getViewDataParkingArea(index) {
        var len = this.listParkingArea[index].parkingSlots.length;
        var idx = 0;
        var indexView = 0;
        var indexCol = 0;
        while (idx != len) {
          this.viewNumber[indexView] = []
          for (let j = 0; j < 8; j++) {
            var newCol = [];
            this.viewNumber[indexView][indexCol] = [];
            for (let i = 0; i < 8; i++) {
              newCol.push(this.listParkingArea[index].parkingSlots[idx])
              idx++;
              if (idx >= len)
                break;
            }
            this.viewNumber[indexView][indexCol] = newCol;
            if (idx >= len)
              break;
            indexCol++;
          }
          indexView++;
        }
      },
      reloadParking() {
        var id = this.listParkingArea[this.parkingAreaViewIndex].id;
        this.getParkingSlotsByIdService(id).then(data3 => {
          this.listParkingArea[this.parkingAreaViewIndex].parkingSlots = data3;
          this.listParkingArea[this.parkingAreaViewIndex].maxNumber = data3.length;
          this.getViewDataParkingArea(this.parkingAreaViewIndex);
          this.parkingAreaDetail = false;
          this.parkingAreaDetail = true;
        })

      },
      getListParkingArea() {
        this.loadingArea=false
        this.listParkingArea = [];
        this.getParkingAreaService().then(data => {
          // data=data.sort((a, b) => (a.id > b.id) ? 1 : -1)
          data.forEach((element) => {
            element.createdDate = this.dateFormat(element.createdDate);
            // this.listParkingArea.push(element);
            // if(this.listParkingAreaPage.length<10)
            //   this.listParkingAreaPage.push(element);
            this.getEmptySlotByParkingAreaId(element.id).then(data2 => {
              element.numNotInUsed = data2;
              this.getParkingSlotsByIdService(element.id).then(data3 => {
                element.parkingSlots = data3
                element.maxNumber = data3.length;
                this.listParkingArea.push(element);
                if(this.listParkingAreaPage.length<10)
                  this.listParkingAreaPage.push(element);
              })

            })
          })

        })
        // this.viewNumber= this.listParkingArea.length;
        // var flag=0
        // this.listParkingArea.forEach(element=>{
        //     this.getEmptySlotByParkingAreaId(element.id).then(data2 => {
        //       element.numNotInUsed = data2;
        //       this.getParkingSlotsByIdService(element.id).then(data3 => {
        //         element.parkingSlots = data3
        //         element.maxNumber = data3.length;
        //         flag++;
        //         console.log(flag)
        //         if(flag==this.listParkingArea.length)
        //           this.loadingArea=false

        //         // this.listParkingArea.push(element);
        //         // if(this.listParkingAreaPage.length<10)
        //         //   this.listParkingAreaPage.push(element);
        //       })

        //     })
        // })
        
      },
      getListParkingSlot() {
        this.getAllParkingSlot().then(data => {
          data
        })
      },
      dateFormat(value) {
        if (value != null) {
          return moment(String(value)).format("yyyy-MM-DD HH:mm:ss");
        } else {
          return value;
        }
      },
      dateFormatEdit(value) {
        if (value != null) {
          return moment(String(value)).format("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        } else {
          return value;
        }
      },
    },
    computed: {
      tableFilter() {
        if (this.tableData) {
          this.tableData.forEach((element) => {
            if (element.type === 1) {
              element.type = "xe máy";
            } else {
              element.type = "ô tô";
            }
          });
        }
      },
    },
    created() {
      this.getListParkingArea();
    }
  };

</script>
