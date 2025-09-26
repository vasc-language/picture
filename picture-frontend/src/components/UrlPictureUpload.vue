<template>
  <div class="url-picture-upload">
    <a-input-group compact style="margin-bottom: 16px">
      <a-input
        v-model:value="fileUrl"
        style="width: calc(100% - 120px)"
        placeholder="请输入图片 URL"
      />
      <a-button type="primary" :loading="loading" @click="handleUpload" style="width: 120px"
        >提交</a-button
      >
    </a-input-group>
    <img v-if="picture?.url" :src="picture?.url" alt="avatar" />
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { message, type UploadProps } from 'ant-design-vue'
import { uploadPictureByUrlUsingPost } from '@/api/pictureController'

const loading = ref<boolean>(false)
const fileUrl = ref<string>()

interface Props {
  picture?: API.PictureVO
  onSuccess?: (newPicture: API.PictureVO) => void
}

const props = defineProps<Props>()

/**
 * 上传
 * @param file
 */
const handleUpload = async ({ file }: any) => {
  loading.value = true
  try {
    const params: API.PictureUploadRequest = { fileUrl: fileUrl.value }
    if (props.picture) {
      params.id = props.picture.id
    }
    const res = await uploadPictureByUrlUsingPost(params)
    if (res.data.code === 0 && res.data.data) {
      message.success('图片上传成功')
      // 将上传的图片信息传递给父组件
      props.onSuccess(res.data.data)
    } else {
      message.error('图片上传失败' + res.data.message)
    }
  } catch (error) {
    message.error('图片上传失败')
  } finally {
    loading.value = false
  }
}

/**
 * 上传前的校验
 *
 * @param file
 */
const beforeUpload = (file: UploadProps['fileList'][number]) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('不支持上传该格式的图片，推荐 jpg 或 png')
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    message.error('不能上传超过 2M 的图片')
  }
  return isJpgOrPng && isLt2M
}
</script>
<style scoped>
.picture-upload :deep(.ant-upload) {
  width: 100% !important;
  height: 100% !important;
  min-height: 152px;
  min-width: 152px;
}

.picture-upload img {
  max-width: 100%;
  max-height: 480px;
}

.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
