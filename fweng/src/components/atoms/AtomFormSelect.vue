<template>
  <div class="form-group mb-3">
    <label :for="id" class="form-label">{{ label }}</label>
    <Field
      :name="name"
      v-slot="{ field, errors, meta }"
      :validate-on-blur="true"
    >
      <select
        v-bind="field"
        :id="id"
        :class="[
          'form-select',
          { 'is-invalid': errors.length && meta.touched },
        ]"
        @change="$emit('update:modelValue', $event.target.value)"
      >
        <option value="">{{ placeholder }}</option>
        <option
          v-for="(option, index) in options"
          :key="index"
          :value="option.value"
        >
          {{ option.text }}
        </option>

        <!-- Optgroup handling (optional) -->
        <optgroup
          v-for="(group, index) in optGroups"
          :key="index"
          :label="group.label"
        >
          <option
            v-for="(option, optIndex) in group.options"
            :key="optIndex"
            :value="option.value"
          >
            {{ option.text }}
          </option>
        </optgroup>
      </select>
    </Field>

    <ErrorMessage :name="name" class="invalid-feedback" />
  </div>
</template>

<script setup>
import { Field, ErrorMessage } from "vee-validate";
import { defineProps } from "vue";

defineProps({
  label: {
    type: String,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
  id: {
    type: String,
    required: true,
  },
  placeholder: {
    type: String,
  },
  options: {
    type: Array,
    default: () => [],
  },
  optGroups: {
    type: Array,
    default: () => [],
  },
});
</script>
