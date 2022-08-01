package com.compact.base.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.compact.base.common.CommonData;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomUserDetails implements UserDetails {
	 
    private static final long serialVersionUID = -4450269958885980297L;
    
    private String userCd;
    private String username;
    private String username2;
    private String password;
    private String compCd;
    private String storCd;
    private String userLv;
    private String misMenuTy;
    private String misUserGrp;
    private String memberNo;
    private String empNo;
    private String hpNo;
    
    
//    public CustomUserDetails(User user) {
//    	this.compCd = user.compCd;
//    	this.userCd = user.userCd;
//    	this.storCd = user.storCd;
//    	this.userLv = user.userLv;
//    	this.username = user.userCd;
//    	this.username2 = user.userNm;
//    	this.password = user.userPs;
//    	this.misMenuTy = user.misMenuTy;
//    	this.misUserGrp = user.misUserGrp;
//    	this.memberNo = user.memberNo;
//    	this.empNo = user.empNo;
//    	this.hpNo = user.hpNo;
//    }
    
    public CustomUserDetails() {
    	
    }

    public CustomUserDetails(String userName, String password)
    {
        this.username = userName;
        this.password = password;
    }
     
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(this.userLv));
        if ( this.userLv.equals(CommonData.UserLv.SYSTEM)) { 
        	authorities.add(new SimpleGrantedAuthority(CommonData.ROLE.ADMIN));
        	authorities.add(new SimpleGrantedAuthority(CommonData.ROLE.MANAGER));
			authorities.add(new SimpleGrantedAuthority(CommonData.ROLE.USER));
		} else if ( this.userLv.equals(CommonData.UserLv.MANAGER)) { 
			authorities.add(new SimpleGrantedAuthority(CommonData.ROLE.MANAGER));
		} else { 
			authorities.add(new SimpleGrantedAuthority(CommonData.ROLE.USER));
		}
        return authorities;
    }
  
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
  
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
  
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
  
    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
   

 }