package com.data.collector.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "machines")
public class Machines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "timeout")
    private int timeout;

    @Column(name = "num_gpus")
    private int num_gpus;

    @Column(name = "total_flops")
    private float total_flops;

    @Column(name = "gpu_name")
    private String gpu_name;

    @Column(name = "gpu_ram")
    private int gpu_ram;

    @Column(name = "gpu_max_cur_temp")
    private int gpu_max_cur_temp;

    @Column(name = "cpu_name")
    private String cpu_name;

    @Column(name = "earn_day")
    private float earn_day;

    @Column(name = "error_description")
    private String error_description;

    public Machines() {
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getNum_gpus() {
        return this.num_gpus;
    }

    public void setNum_gpus(int num_gpus) {
        this.num_gpus = num_gpus;
    }

    public float getTotal_flops() {
        return this.total_flops;
    }

    public void setTotal_flops(float total_flops) {
        this.total_flops = total_flops;
    }

    public String getGpu_name() {
        return this.gpu_name;
    }

    public void setGpu_name(String gpu_name) {
        this.gpu_name = gpu_name;
    }

    public int getGpu_ram() {
        return this.gpu_ram;
    }

    public void setGpu_ram(int gpu_ram) {
        this.gpu_ram = gpu_ram;
    }

    public int getGpu_max_cur_temp() {
        return this.gpu_max_cur_temp;
    }

    public void setGpu_max_cur_temp(int gpu_max_cur_temp) {
        this.gpu_max_cur_temp = gpu_max_cur_temp;
    }

    public String getCpu_name() {
        return this.cpu_name;
    }

    public void setCpu_name(String cpu_name) {
        this.cpu_name = cpu_name;
    }

    public float getEarn_day() {
        return this.earn_day;
    }

    public void setEarn_day(float earn_day) {
        this.earn_day = earn_day;
    }

    public String getError_description() {
        return this.error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", hostname='" + getHostname() + "'" + ", timeout='" + getTimeout() + "'"
                + ", num_gpus='" + getNum_gpus() + "'" + ", total_flops='" + getTotal_flops() + "'" + ", gpu_name='"
                + getGpu_name() + "'" + ", gpu_ram='" + getGpu_ram() + "'" + ", gpu_max_cur_temp='"
                + getGpu_max_cur_temp() + "'" + ", cpu_name='" + getCpu_name() + "'" + ", earn_day='" + getEarn_day()
                + "'" + ", error_description='" + getError_description() + "'" + "}";
    }

}
