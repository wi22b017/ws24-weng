<template>
  <div class="form-group mb-3">
    <label :for="id" class="form-label">{{ label }}</label>
    <Field
        :name="name"
        v-slot="{ field, errors, meta }"
        :validate-on-blur="true"
    >
      <input
          :id="id"
          :type="type"
          class="form-control"
          v-bind="field"
          :class="{ 'is-invalid': errors.length && meta.touched }"
          :placeholder="placeholder"
          :min="min"
          @input="onInput($event)"
      />
    </Field>
    <ErrorMessage :name="name" class="invalid-feedback" />
  </div>
</template>

<script setup>
import { Field, ErrorMessage } from "vee-validate";
import { defineProps, defineEmits } from "vue";

const props = defineProps({
  label: String,
  name: String,
  type: {
    type: String,
    default: "text",
  },
  id: String,
  placeholder: String,
  modelValue: [String, Number],
  min: [String, Number],
});

const emit = defineEmits(["update:modelValue"]);

const onInput = (event) => {
  let value = event.target.value;
  if (props.type === "number") {
    value = Number(value);
  }
  emit("update:modelValue", value);
};
</script>
