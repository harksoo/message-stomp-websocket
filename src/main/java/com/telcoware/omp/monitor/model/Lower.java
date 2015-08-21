package com.telcoware.omp.monitor.model;

import java.util.ArrayList;

public class Lower
{
  private String lower;
  private ArrayList<Item> value;

  public String getLower()
  {
    return this.lower; }

  public void setLower(String lower) {
    this.lower = lower; }

  public ArrayList<Item> getValue() {
    return this.value; }

  public void setValue(ArrayList<Item> value) {
    this.value = value;
  }
}