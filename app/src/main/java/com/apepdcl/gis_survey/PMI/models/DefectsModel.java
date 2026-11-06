package com.apepdcl.gis_survey.PMI.models;


/**
 * Created by SOUBHAGYA SWAIN on 21-03-2025.
 */
public class DefectsModel {

 private String DEFECT_ID,DEFECT_TYPE,DEFECT_SUB_TYPE,DEFECT_SUB_TYPE_ID,VOLTAGE_LEVEL;

 private String AI, PHOTO, GRP_ID, DEFECT_GROUP;

 public String getAI() {
  return AI;
 }

 public void setAI(String AI) {
  this.AI = AI;
 }

 public String getPHOTO() {
  return PHOTO;
 }

 public void setPHOTO(String PHOTO) {
  this.PHOTO = PHOTO;
 }

 public String getGRP_ID() {
  return GRP_ID;
 }

 public void setGRP_ID(String GRP_ID) {
  this.GRP_ID = GRP_ID;
 }

 public String getDEFECT_GROUP() {
  return DEFECT_GROUP;
 }

 public void setDEFECT_GROUP(String DEFECT_GROUP) {
  this.DEFECT_GROUP = DEFECT_GROUP;
 }


 private boolean isSelected;
 private boolean isImageSelected;
 String image;

 public String getImage() {
  return image;
 }

 public void setImage(String image) {
  this.image = image;
 }

 private String imagePath;

 public boolean isSelected() {
  return isSelected;
 }

 public void setSelected(boolean selected) {
  isSelected = selected;
 }

 public boolean isImageSelected() {
  return isImageSelected;
 }

 public void setImageSelected(boolean imageSelected) {
  isImageSelected = imageSelected;
 }

 public String getImagePath() {
  return imagePath;
 }

 public void setImagePath(String imagePath) {
  this.imagePath = imagePath;
 }

 public String getDEFECT_ID() {
  return DEFECT_ID;
 }

 public void setDEFECT_ID(String DEFECT_ID) {
  this.DEFECT_ID = DEFECT_ID;
 }

 public String getDEFECT_TYPE() {
  return DEFECT_TYPE;
 }

 public void setDEFECT_TYPE(String DEFECT_TYPE) {
  this.DEFECT_TYPE = DEFECT_TYPE;
 }

 public String getDEFECT_SUB_TYPE() {
  return DEFECT_SUB_TYPE;
 }

 public void setDEFECT_SUB_TYPE(String DEFECT_SUB_TYPE) {
  this.DEFECT_SUB_TYPE = DEFECT_SUB_TYPE;
 }

 public String getDEFECT_SUB_TYPE_ID() {
  return DEFECT_SUB_TYPE_ID;
 }

 public void setDEFECT_SUB_TYPE_ID(String DEFECT_SUB_TYPE_ID) {
  this.DEFECT_SUB_TYPE_ID = DEFECT_SUB_TYPE_ID;
 }

 public String getVOLTAGE_LEVEL() {
  return VOLTAGE_LEVEL;
 }

 public void setVOLTAGE_LEVEL(String VOLTAGE_LEVEL) {
  this.VOLTAGE_LEVEL = VOLTAGE_LEVEL;
 }
}
