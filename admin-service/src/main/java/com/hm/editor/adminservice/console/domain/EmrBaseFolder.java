package com.hm.editor.adminservice.console.domain;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;


/**
 * @author:wanglei
 * @date:2020/10/15
 * @desc:
 */
public class EmrBaseFolder implements Serializable,ObjectSerializer {
  private static final long serialVersionUID = 89843781789L;
  private ObjectId _id;
  private String name;
  private int order;
  private String idStr;

  public ObjectId get_id() {
    return _id;
  }

  public void set_id(ObjectId _id) {
    this._id = _id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public String getIdStr() {
    return idStr;
  }

  public void setIdStr(String idStr) {
    this.idStr = idStr;
  }

  @Override
  public String toString() {
    return "{name:"+name+"}";
  }

  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {

    System.out.println(fieldName);
    System.out.println(object);
    serializer.write(object);
  }

  public void initIDStr(){
    if(_id == null){
      idStr = "";
    }else {
      idStr = _id.toHexString();
    }
  }
}
