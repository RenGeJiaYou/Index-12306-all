--[[KEYS 全部不存在，创建并返回 true，反之返回空]]

-- 1. 遍历所有的KEYS，如果有一个KEY存在，返回空
for i, v in ipairs(KEYS) do
    if(redis.call("exist",v) == 1) then
        return nil
    end
end

-- 2. 遍历所有的KEYS，设置键 v 为 default ，并设置过期时间为 ARGV[1]
for i, v in ipairs(KEYS) do
    redis.call("set",v,"default");
    redis.call("pexpire",v,ARGV[1]); --ARGV 是一个特殊的变量，它包含了所有传入脚本的参数;从 [1] 开始
end
return true;