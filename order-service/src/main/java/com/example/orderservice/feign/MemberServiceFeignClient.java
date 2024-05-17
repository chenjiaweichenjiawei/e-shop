package com.example.orderservice.feign;

import com.example.orderservice.vo.MemberAddressVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author CJW
 * @since 2024/5/15
 */
@FeignClient("member-service")
public interface MemberServiceFeignClient {
    @GetMapping("/memberReceiveAddress/{memberId}/addresses")
    List<MemberAddressVO> getAddress(@PathVariable("memberId") Long memberId);
}
