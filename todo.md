# TODO

- [x] 分析 `tslib` 缺失问题并确认修复方案
- [x] 在 `package.json` 添加 `tslib` 依赖并同步锁文件
- [x] 运行 `npm run openapi` 验证命令通过
- [x] 在本文件新增 review 小结

## Review
- 添加 `tslib@^2.8.1` 满足 `@umijs/openapi` 的运行依赖
- `npm run openapi` 可正常执行并生成服务文件
- PictureServiceImpl 审核状态比较采用 `Objects.equals`，避免旧数据 `reviewStatus` 为空时触发空指针
- PictureController.updatePicture 在持久化前对当前 picture 调用 `fillReviewParams`，确保审核字段同步更新
- 未运行后端自动化测试（改动较小，建议后续补充验证）

- [x] 通读 picture-backend 审核流程相关代码并确认问题
- [x] 更新 PictureServiceImpl 审核状态比较逻辑避免空指针
- [x] 自检或说明未运行测试
- [x] 补充 review 小结
- [x] 通读 picture-backend 控制层审核流程确认 review 参数更新问题
- [x] 调整 PictureController.updatePicture 中 fillReviewParams 调用对象
- [x] 自检或说明未运行测试
- [x] 更新 review 小结
- [x] 编写仓库贡献指南 AGENTS.md 草案
- [x] 汇总前后端结构、构建与规范信息

## Review（贡献指南）

- 新增 `AGENTS.md` 总结项目结构、构建命令、编码规范与提交流程
- 将前后端常用命令与工具链说明集中到单一文档便于贡献者查阅
- 文档修改未涉及代码路径，未执行自动化测试（文档变更无需）
- [x] 确认用户编辑图片时审核参数缺失原因
- [x] 在 PictureController.editPicture 中补充 fillReviewParams 调用

## Review（编辑审核）

- 用户在 `editPicture` 接口提交修改时调用 `fillReviewParams`，确保非管理员内容重回审核流程
- 保持管理员编辑时的自动过审逻辑一致
- 仅改动控制层逻辑，未执行自动化测试（建议补充回归）
- [ ] 确认当前分支与远程状态
- [ ] 整理并提交本次修改
- [ ] 推送至 GitHub 远程仓库
