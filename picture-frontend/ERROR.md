"D:\Program Files\nodejs\npm.cmd" run openapi

> picture-frontend@0.0.0 openapi
> node openapi.config.js

node:internal/modules/cjs/loader:1225
const err = new Error(message);
^

Error: Cannot find module 'tslib'
Require stack:
- D:\Java\picture\picture-frontend\node_modules\@umijs\openapi\dist\index.js
  at Function._resolveFilename (node:internal/modules/cjs/loader:1225:15)
  at Function._load (node:internal/modules/cjs/loader:1055:27)
  at TracingChannel.traceSync (node:diagnostics_channel:322:14)
  at wrapModuleLoad (node:internal/modules/cjs/loader:220:24)
  at Module.require (node:internal/modules/cjs/loader:1311:12)
  at require (node:internal/modules/helpers:136:16)
  at Object.<anonymous> (D:\Java\picture\picture-frontend\node_modules\@umijs\openapi\dist\index.js:4:17)
  at Module._compile (node:internal/modules/cjs/loader:1554:14)
  at Object..js (node:internal/modules/cjs/loader:1706:10)
  at Module.load (node:internal/modules/cjs/loader:1289:32) {
  code: 'MODULE_NOT_FOUND',
  requireStack: [
  'D:\\Java\\picture\\picture-frontend\\node_modules\\@umijs\\openapi\\dist\\index.js'
  ]
  }

Node.js v22.14.0

Process finished with exit code 1