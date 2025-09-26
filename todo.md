# TODO

- [x] 核对 PictureServiceImpl 非管理员审核逻辑并确认需要重置的审核字段
- [x] 更新 fillReviewParams 清空 reviewMessage / reviewerId / reviewTime 并允许空值更新
- [x] 在 todo.md 记录本次修复思路与 review 摘要
- [x] 说明测试执行情况（未运行自动化测试，建议补充回归）
- [x] 通读 picture-frontend 关键视图（Home / Add / Manage / Detail 等）评估审核流程相关实现
- [x] 分析图片审核前端逻辑是否存在导致功能失效的严重缺陷
- [x] 在 todo.md 记录前端巡检结论与后续建议

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

## Review（重置审核字段）

- 非管理员编辑图片时将审核状态重置为待审核，同时清空 `reviewMessage` / `reviewerId` / `reviewTime`
- 为审核元数据字段新增 `updateStrategy = FieldStrategy.IGNORED`，确保 `updateById` 可写入空值
- 未运行自动化测试（后续可补充回归）

## Review（前端审核流程巡检）

- 主页、上传、详情及管理页数据流与审核接口对接正常，未发现阻断审核流程的严重缺陷
- 管理端筛选使用的审核状态下拉以字符串值传参，Jackson 会自动转换为整数，不影响过滤逻辑
- 管理端审核信息仍显示 `reviewerId` 裸 ID，体验欠佳但不影响审核功能
- 未执行前端构建或自动化测试，后续可按需补充 `npm run build` / `npm run type-check`
- [ ] 确认当前分支与远程状态
- [ ] 整理并提交本次修改
- [ ] 推送至 GitHub 远程仓库

## Review（URL 下载安全问题）
- [x] 排查 `UrlPictureUpload` 下载实现，确认基于 `HttpUtil.downloadFile` 的处理流程
- [x] 识别未做主机校验导致的 SSRF 风险，以及缺乏有效的响应体大小约束
- [x] 建议引入 URL 白名单 / 内网阻断策略，并根据 `Content-Length` 或流式读取设置下载上限

    - UrlPictureUpload.processFile 在 picture-backend/src/main/java/com/spring/picturebackend/manager/upload/
      UrlPictureUpload.java:37 直接对用户输入的任意 URL 调用 HttpUtil.downloadFile，未做域名/IP 白名单或内网网段拦
      截；攻击者可借此让服务器主动访问内网服务（如 http://127.0.0.1:8080/actuator、http://169.254.169.254/latest/meta-
      data/），这属于典型 SSRF 高危漏洞。
    - validPicture 的校验不足（UrlPictureUpload.java:57 起）：
        - 只校验协议与基本头信息，未阻断内网地址；
        - HEAD 返回非 200 时直接 return，等于跳过所有限制仍执行下载；
        - 若响应缺少 Content-Length，2MB 限制失效，可被诱导下载超大文件导致磁盘或流量爆炸。

  修复建议

    1. 建立允许列表或解析 IP 后拒绝私有网段/本机地址；对跳转后的最终地址也要重复校验。
    2. 使用带限速/限长的流式下载，实时统计已读字节并在超过阈值时终止。
    3. HEAD 失败应直接抛错，禁止继续 GET；同时配置合理的连接/读取超时时间并限制重定向次数。
    4. 视安全需求增加文件类型白名单的实际内容检测（如读取文件头校验）。
- [x] 复核 `validPicture` 在 HEAD 非 200 时直接返回的问题
- [x] 说明未限制内网主机与缺少实际下载大小限制的风险

- [x] 校验 URL 主机解析到内网 / 环回地址时直接拦截
- [x] HEAD 非 200 时抛错，避免绕过校验
- [x] 要求 `Content-Length` 存在且不超过 2MB

## Review（URL 校验优化）
- 强化 `UrlPictureUpload.validPicture`，拦截内网主机、HEAD 非 200 与缺失大小信息
- HEAD 校验强制 200 成功后才继续下载，避免安全校验被绕过
- 未新增自动化测试（仅调整后端参数校验，建议后续补充针对 URL 上传路径的集成测试）
