@echo off 
::set work_path=E:\workspace\Server\Projects\Mars-Protocol\src\main\thrift  
::cd %work_path% 
set thriftPath=.\src\main\thrift
set javaOut=.\src\main\java
::set csharpOut=..\..\..\Client\Assets\Scripts
set csharpOut=E:\项目三部\Client\Assets\Scripts\
for /R %thriftPath%\server %%s in (*) do ( 
thrift -gen java:beans -out %javaOut% %%s
echo %%s 
)

for /R %thriftPath%\client %%s in (*) do ( 
thrift -gen java:beans -out %javaOut% %%s
thrift -gen csharp -out %csharpOut% %%s
echo %%s 
) 
pause