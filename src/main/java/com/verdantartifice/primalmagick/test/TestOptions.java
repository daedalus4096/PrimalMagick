package com.verdantartifice.primalmagick.test;

public record TestOptions(int timeoutTicks, String batch, boolean skyAccess, int rotationSteps, boolean required, boolean manualOnly, String template, long setupTicks, int attempts, int requiredSuccesses) {
    public static final TestOptions DEFAULT = new TestOptions(100, TestUtils.DEFAULT_BATCH, false, 0, true, false, TestUtils.DEFAULT_TEMPLATE, 0L, 1, 1);
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private int timeoutTicks = DEFAULT.timeoutTicks();
        private String batch = DEFAULT.batch();
        private boolean skyAccess = DEFAULT.skyAccess();
        private int rotationSteps = DEFAULT.rotationSteps();
        private boolean required = DEFAULT.required();
        private boolean manualOnly = DEFAULT.manualOnly();
        private String template = DEFAULT.template();
        private long setupTicks = DEFAULT.setupTicks();
        private int attempts = DEFAULT.attempts();
        private int requiredSuccesses = DEFAULT.requiredSuccesses();
        
        public Builder timeoutTicks(int ticks) {
            this.timeoutTicks = ticks;
            return this;
        }
        
        public Builder batch(String batch) {
            this.batch = batch;
            return this;
        }
        
        public Builder skyAccess() {
            this.skyAccess = true;
            return this;
        }
        
        public Builder rotationSteps(int steps) {
            this.rotationSteps = steps;
            return this;
        }
        
        public Builder optional() {
            this.required = false;
            return this;
        }
        
        public Builder manualOnly() {
            this.manualOnly = true;
            return this;
        }
        
        public Builder template(String template) {
            this.template = template;
            return this;
        }
        
        public Builder setupTicks(long ticks) {
            this.setupTicks = ticks;
            return this;
        }
        
        public Builder attempts(int attempts) {
            this.attempts = attempts;
            return this;
        }
        
        public Builder requiredSuccesses(int successes) {
            this.requiredSuccesses = successes;
            return this;
        }
        
        public TestOptions build() {
            return new TestOptions(this.timeoutTicks, this.batch, this.skyAccess, this.rotationSteps, this.required, this.manualOnly, this.template, this.setupTicks, this.attempts, this.requiredSuccesses);
        }
    }
}
