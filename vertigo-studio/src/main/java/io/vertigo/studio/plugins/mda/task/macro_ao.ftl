<#macro generateHeader taskDefinitions>
	/** Liste des taches. */
	private static enum Tasks {
<#list taskDefinitions as taskDefinition>
		/** Tache ${taskDefinition.urn} */
		${taskDefinition.urn},
</#list>
	}

<#list taskDefinitions as taskDefinition>
	<#list taskDefinition.attributes as taskAttribute>
	/** Constante de paramètre de la tache ${taskAttribute.name}. */
	private static final String ${taskAttribute.constantName} = "${taskAttribute.name}";

	</#list>
</#list>
	private final TaskManager taskManager;
</#macro> 
   
<#macro generateBody taskDefinitions>

	/**
	 * Création d'une tache.
	 * @param task Type de la tache
	 * @return Builder de la tache
	 */
	private TaskBuilder createTaskBuilder(final Tasks task) {
		final TaskDefinition taskDefinition = Home.getDefinitionSpace().resolve(task.toString(), TaskDefinition.class);
		return new TaskBuilder(taskDefinition);
	}

<#list taskDefinitions as taskDefinition>
	/**
	 * Execute la tache ${taskDefinition.urn}.
	<#list taskDefinition.inAttributes as taskAttribute>
	 * @param ${taskAttribute.variableName} ${taskAttribute.dataType} <#if !taskAttribute.notNull>(peut être null)</#if>
	</#list>
     <#if taskDefinition.out>
	 * @return <#if !taskDefinition.outAttribute.notNull>Option de </#if>${taskDefinition.outAttribute.dataType} ${taskDefinition.outAttribute.variableName}
	</#if>
	*/
	public <#if taskDefinition.out><#if !taskDefinition.outAttribute.notNull>Option<</#if>${taskDefinition.outAttribute.dataType}<#if !taskDefinition.outAttribute.notNull>></#if><#else>void</#if> ${taskDefinition.methodName}(<#list taskDefinition.inAttributes as taskAttribute>final <#if !taskAttribute.notNull>Option<</#if>${taskAttribute.dataType}<#if !taskAttribute.notNull>></#if> ${taskAttribute.variableName}<#if taskAttribute_has_next>, </#if></#list>) {
		final Task task = createTaskBuilder(Tasks.${taskDefinition.urn})//
	<#list taskDefinition.inAttributes as taskAttribute>
				.withValue(${taskAttribute.constantName}, ${taskAttribute.variableName}<#if !taskAttribute.notNull>.getOrElse(null)</#if>)//
    </#list>
				.build();
    <#if taskDefinition.out>
		final TaskResult taskResult = taskManager.execute(task);
		return <#if !taskDefinition.outAttribute.notNull>Option.option(</#if>taskResult.<${taskDefinition.outAttribute.dataType}> getValue(${taskDefinition.outAttribute.constantName})<#if !taskDefinition.outAttribute.notNull>)</#if>;
    <#else>
		taskManager.execute(task);
    </#if>
	}

</#list>
</#macro>
