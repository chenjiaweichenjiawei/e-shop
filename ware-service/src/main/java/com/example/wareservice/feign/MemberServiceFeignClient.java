package com.example.wareservice.feign;

import com.example.common.util.Result;
import com.example.wareservice.vo.MemberAddressVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author CJW
 * @since 2024/5/16
 */
@FeignClient("member-service")
public interface MemberServiceFeignClient {
    @GetMapping("/memberReceiveAddress/info/{id}")
    Result<MemberAddressVO> addrInfo(@PathVariable("id") Long id);
}
