@echo off 
:: thrift脚本文件目录
set thriftPath=.\src\main\thrift
:: java代码输出路径，目前直接配置为项目的代码路径
set javaOut=.\src\main\java
:: c#代码输出路径《酷跑》
set csharpOut=..\..\..\Client\Assets\Scripts
:: c#代码输出路径《消消乐》，根据本机实际情况修改目录
set csharpOut2=E:\项目三部\Client\Assets\Scripts\
:: 生成仅服务器使用的代码，主要是RPC服务相关脚本
for /R %thriftPath%\server %%s in (*) do ( 
thrift -gen java:beans -out %javaOut% %%s
echo %%s 
)
:: 生成服务器及客户端公用代码
for /R %thriftPath%\client %%s in (*) do ( 
thrift -gen java:beans -out %javaOut% %%s
thrift -gen csharp -out %csharpOut% %%s
thrift -gen csharp -out %csharpOut2% %%s
echo %%s 
) 
pause