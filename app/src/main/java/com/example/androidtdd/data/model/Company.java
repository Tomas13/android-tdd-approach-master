package com.example.androidtdd.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by quanlt on 19/01/2017.
 */

public class Company implements Parcelable {
    private String catchPhrase;
    private String name;
    private String bs;

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.catchPhrase);
        dest.writeString(this.name);
        dest.writeString(this.bs);
    }

    public Company() {
    }

    protected Company(Parcel in) {
        this.catchPhrase = in.readString();
        this.name = in.readString();
        this.bs = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel source) {
            return new Company(source);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    @Override
    public String toString() {
        return "Company{" +
                "catchPhrase='" + catchPhrase + '\'' +
                ", name='" + name + '\'' +
                ", bs='" + bs + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;

        Company company = (Company) o;

        if (catchPhrase != null ? !catchPhrase.equals(company.catchPhrase) : company.catchPhrase != null)
            return false;
        if (name != null ? !name.equals(company.name) : company.name != null) return false;
        return bs != null ? bs.equals(company.bs) : company.bs == null;

    }

    @Override
    public int hashCode() {
        int result = catchPhrase != null ? catchPhrase.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (bs != null ? bs.hashCode() : 0);
        return result;
    }
}
