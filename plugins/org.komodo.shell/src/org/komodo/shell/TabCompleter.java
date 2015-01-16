/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.komodo.shell;

import java.util.ArrayList;
import java.util.List;
import org.jboss.aesh.complete.CompleteOperation;
import org.jboss.aesh.complete.Completion;
import org.komodo.shell.api.Arguments;
import org.komodo.shell.api.InvalidCommandArgumentException;
import org.komodo.shell.api.ShellCommand;
import org.komodo.shell.commands.CommandNotFoundCommand;
import org.komodo.utils.KLog;

/**
 * Implements tab completion for the interactive
 * 
 * This class adapted from https://github.com/Governance/s-ramp/blob/master/s-ramp-shell
 * - altered complete method due to removal of shell context
 * 
 * @author eric.wittmann@redhat.com
 */
public class TabCompleter implements Completion {

    private final ShellCommandFactory factory;

    /**
     * Constructor.
     *
     * @param factory the factory
     */
    public TabCompleter(ShellCommandFactory factory) {
        this.factory = factory;
    }

    /**
     * @param completeOperation the complete operation
     */
    @Override
    public void complete(CompleteOperation completeOperation) {
    	String buffer = completeOperation.getBuffer();
    	List<String> allCommandsForContext = new ArrayList<String>();
        try {
            allCommandsForContext.addAll(factory.getCommandsForCurrentContext());
        } catch (Exception ex) {
            KLog.getLogger().error(ex.getMessage(), ex);
        }

    	// Case 1 - nothing has been typed yet - show all commands for this context
    	if (buffer.trim().length() == 0) {

    		for(String commandName : allCommandsForContext) {
    			completeOperation.addCompletionCandidate(commandName);
    		}
    		// Case 2 - partial command has been typed - show entire command
    	} else if(!buffer.contains(" ")) { //$NON-NLS-1$
    		String name = buffer.toString().trim();
    		for (String cmdName : allCommandsForContext) {
    			if (cmdName.startsWith(name)) {
    				completeOperation.addCompletionCandidate(cmdName);
    			}
    		}
    	} else {
    		// We check the first thing if what is introduced is a command or not.
    		// Check if what was introduced is a command itself
    		Arguments arguments = null;
    		try {
    			arguments = new Arguments(buffer, true);
    		} catch (InvalidCommandArgumentException e1) {
    			// should never happen...but if it does, just bail
    		}
    		String commandName = arguments.removeCommandName();
    		String lastArgument = null;
    		if (arguments.size() > 0 && !buffer.endsWith(" ")) { //$NON-NLS-1$
    			lastArgument = arguments.remove(arguments.size() - 1);
    		}
    		ShellCommand command = null;
    		try {
    			command = factory.getCommand(commandName);
    		} catch (Exception ex) {
    		    KLog.getLogger().error(ex.getMessage(), ex);
    		}

    		// In case it is a command then we print the tabCompletion
    		// specific of the command
    		if (command != null && !(command instanceof CommandNotFoundCommand)) {
    			command.setArguments(arguments);

    			List<CharSequence> list = new ArrayList<CharSequence>();
    			int tabCompletionResult = -1;
                try {
                    tabCompletionResult = command.tabCompletion(lastArgument, list);
                } catch (Exception ex) {
                    KLog.getLogger().error(ex.getMessage(), ex);
                }

    			if (!list.isEmpty()) {
    				// In case the tab completion return just one result it
    				// is printed the previous buffer plus the argument
    				if (list.size() == 1) {
    					if (buffer.endsWith(" ")) { //$NON-NLS-1$
    						completeOperation.addCompletionCandidate(buffer
    								+ list.get(0).toString().trim());
    					} else if (buffer.indexOf(" ") != -1) { //$NON-NLS-1$
    						completeOperation.addCompletionCandidate(buffer.substring(0,
    								buffer.lastIndexOf(" ")) //$NON-NLS-1$
    								+ " " + list.get(0).toString().trim()); //$NON-NLS-1$
    					} else {
    						completeOperation.addCompletionCandidate(buffer + " " //$NON-NLS-1$
    								+ list.get(0).toString().trim());
    					}

    				} else {
    					// In case the result of the command tab completion
    					// contains more than one result (like the FileNameCompleter)
    					for (CharSequence sequence : list) {
    						completeOperation.addCompletionCandidate(sequence.toString());
    					}
    				}
    				if (tabCompletionResult == CompletionConstants.NO_APPEND_SEPARATOR) {
    					completeOperation.doAppendSeparator(false);
    				}

    			}
    		}

    	}
    }

//        String commonPartCandidates = mergeCandidates(completeOperation.getCompletionCandidates(), buffer);
//        if (StringUtils.isNotBlank(commonPartCandidates)) {
//            String tokenToCompare = "";
//            if (buffer.contains(" ")) {
//                tokenToCompare = buffer.substring(buffer.lastIndexOf(" ") + 1);
//            } else {
//                tokenToCompare = buffer;
//            }
//            completeOperation.getCompletionCandidates().clear();
//            if (StringUtils.isBlank(tokenToCompare) || commonPartCandidates.startsWith(tokenToCompare)) {
//                if (buffer.contains(" ")) {
//                    completeOperation.addCompletionCandidate(buffer.substring(0, buffer.lastIndexOf(" "))
//                            .trim() + " " + commonPartCandidates);
//                } else {
//                    completeOperation.addCompletionCandidate(commonPartCandidates);
//                }
//
//            } else {
//                completeOperation.addCompletionCandidate(commonPartCandidates);
//            }
//
//            completeOperation.doAppendSeparator(false);
//        }

    /**
     * Merge candidates.
     *
     * @param completionCandidates the completion candidates
     * @param buffer the buffer
     * @return the string
     */
//    private String mergeCandidates(List<String> completionCandidates, String buffer) {
//        if (completionCandidates.size() > 1) {
//            int indexOfDifference = StringUtils.indexOfDifference(completionCandidates
//                    .toArray(new String[completionCandidates.size()]));
//            if (indexOfDifference == -1) {
//                return completionCandidates.get(0);
//            } else {
//                String partToCompare = "";
//                String commonPart = completionCandidates.get(0).substring(0, indexOfDifference);
//                if (commonPart.startsWith(buffer)) {
//                    partToCompare = buffer;
//                } else {
//                    partToCompare = buffer.substring(buffer.lastIndexOf(" ") + 1);
//                }
//                if (partToCompare.length() != indexOfDifference) {
//                    return commonPart;
//                }
//
//            }
//        }
//        return "";
//
//    }
}
