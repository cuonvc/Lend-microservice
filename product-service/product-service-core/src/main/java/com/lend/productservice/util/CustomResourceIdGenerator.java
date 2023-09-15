//package com.lend.productservice.util;
//
//import org.hibernate.engine.spi.SharedSessionContractImplementor;
//import org.hibernate.id.IdentifierGenerator;
//
//import java.util.UUID;
//
//public class CustomResourceIdGenerator implements IdentifierGenerator {
//
//    @Override
//    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
//        return "Img" + UUID.randomUUID().toString().replaceAll("-", "");
//    }
//}
