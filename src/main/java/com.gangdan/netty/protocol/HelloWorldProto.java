/*
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: main/resources/person.proto

package com.gangdan.netty.protocol;

public final class HelloWorldProto {
  private HelloWorldProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gangdan_netty_protocol_Person_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gangdan_netty_protocol_Person_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\033main/resources/person.proto\022\032com.gangd" +
      "an.netty.protocol\"1\n\006Person\022\n\n\002id\030\001 \001(\005\022" +
      "\014\n\004name\030\002 \001(\t\022\r\n\005email\030\003 \001(\tB\026B\017HelloWor" +
      "ldProtoP\001\210\001\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_gangdan_netty_protocol_Person_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_gangdan_netty_protocol_Person_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gangdan_netty_protocol_Person_descriptor,
        new String[] { "Id", "Name", "Email", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
*/