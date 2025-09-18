# TODO

- [x] 分析 `tslib` 缺失问题并确认修复方案
- [x] 在 `package.json` 添加 `tslib` 依赖并同步锁文件
- [x] 运行 `npm run openapi` 验证命令通过
- [x] 在本文件新增 review 小结

## Review
- 添加 `tslib@^2.8.1` 满足 `@umijs/openapi` 的运行依赖
- `npm run openapi` 可正常执行并生成服务文件
