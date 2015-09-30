namespace java thrift.pojo
namespace csharp Thrift.pojo

enum LoginCode
{
	OK = 1,
	ERROR = 2
}

struct RequestUserLogin
{
	1: string username,
	2: string password,
}

struct ResponseUserLogin
{
	1: LoginCode code,
	2: string msg
}

