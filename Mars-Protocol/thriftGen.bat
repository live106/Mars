@echo off 
:: thrift�ű��ļ�Ŀ¼
set thriftPath=.\src\main\thrift
:: java�������·����Ŀǰֱ������Ϊ��Ŀ�Ĵ���·��
set javaOut=.\src\main\java
:: c#�������·�������ܡ�
set csharpOut=..\..\..\Client\Assets\Scripts
:: c#�������·���������֡������ݱ���ʵ������޸�Ŀ¼
set csharpOut2=E:\��Ŀ����\Client\Assets\Scripts\
:: ���ɽ�������ʹ�õĴ��룬��Ҫ��RPC������ؽű�
for /R %thriftPath%\server %%s in (*) do ( 
thrift -gen java:beans -out %javaOut% %%s
echo %%s 
)
:: ���ɷ��������ͻ��˹��ô���
for /R %thriftPath%\client %%s in (*) do ( 
thrift -gen java:beans -out %javaOut% %%s
thrift -gen csharp -out %csharpOut% %%s
thrift -gen csharp -out %csharpOut2% %%s
echo %%s 
) 
pause